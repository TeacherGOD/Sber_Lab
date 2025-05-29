package com.sber_ii_lab.service;

import com.sber_ii_lab.dto.request.ProjectRequest;
import com.sber_ii_lab.dto.response.ProjectDto;
import com.sber_ii_lab.entity.Author;
import com.sber_ii_lab.entity.Project;
import com.sber_ii_lab.entity.Tag;
import com.sber_ii_lab.exception.EntityNotFoundException;
import com.sber_ii_lab.mapper.ProjectMapper;
import com.sber_ii_lab.repository.AuthorRepository;
import com.sber_ii_lab.repository.ProjectRepository;
import com.sber_ii_lab.repository.TagRepository;
import com.sber_ii_lab.repository.specification.ProjectSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final ProjectMapper projectMapper;
    private final FileStorageService fileStorageService;
    private final TagService tagService;
    private final AuthorService authorService;
    private final ScheduledExecutorService taskScheduler;

    @Transactional
    @PreAuthorize("hasRole('USER')")
    public ProjectDto createProject(ProjectRequest request) {
        String imageUrl = fileStorageService.storeFile(request.getImage());

        Project project = projectMapper.toEntity(request);
        project.setImageUrl(imageUrl);

        Set<Author> authors = request.getAuthorIds().stream()
                .map(authorId -> authorRepository.findById(authorId)
                        .orElseThrow(() -> new EntityNotFoundException("Автор не найден: " + authorId)))
                .collect(Collectors.toSet());

        Set<Tag> tags = request.getTagNames().stream()
                .map(tagName -> tagRepository.findByNameIgnoreCase(tagName)
                        .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build())))
                .collect(Collectors.toSet());

        project.setAuthors(authors);
        project.setTags(tags);

        Project savedProject = projectRepository.save(project);
        return projectMapper.toDto(savedProject);
    }

    @Transactional(readOnly = true)
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('USER')")
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));

        String imageUrl = project.getImageUrl();

        projectRepository.delete(project);

        taskScheduler.schedule(() -> {
            tagService.cleanupUnusedTags();
            authorService.cleanupUnusedAuthors();
            fileStorageService.deleteFileIfUnused(imageUrl);
        }, 1, TimeUnit.MINUTES);
    }

    @Transactional
    @PreAuthorize("hasRole('USER')")
    public ProjectDto updateProject(Long id, ProjectRequest request) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));

        String newImageUrl = fileStorageService.updateFile(
                request.getImage(),
                existingProject.getImageUrl()
        );

        projectMapper.updateProjectFromRequest(request, existingProject);
        existingProject.setImageUrl(newImageUrl);

        updateAuthors(existingProject, request.getAuthorIds());
        updateTags(existingProject, request.getTagNames());

        return projectMapper.toDto(projectRepository.save(existingProject));
    }

    @PreAuthorize("hasRole('USER')")
    private void updateAuthors(Project project, Set<Long> authorIds) {
        List<Author> authors = authorRepository.findAllById(authorIds);
        project.setAuthors(new HashSet<>(authors));
    }

    @PreAuthorize("hasRole('USER')")
    private void updateTags(Project project, Set<String> tagNames) {
        Set<Tag> tags = tagNames.stream()
                .map(name -> tagRepository.findByNameIgnoreCase(name)
                        .orElseGet(() -> tagRepository.save(Tag.builder().name(name).build())))
                .collect(Collectors.toSet());
        project.setTags(tags);
    }

    @Transactional(readOnly = true)
    public ProjectDto getProjectById(Long id) {
        return projectRepository.findById(id)
                .map(projectMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Проект не найден"));
    }

    @Transactional(readOnly = true)
    public Page<ProjectDto> getProjectsPage(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<ProjectDto> searchProjects(String name, List<String> tags) {
        Specification<Project> spec = ProjectSpecifications.combineSpecifications(name, tags);
        return projectRepository.findAll(spec).stream()
                .map(projectMapper::toDto)
                .toList();
    }
}
