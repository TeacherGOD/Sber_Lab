package com.sber_ii_lab.service;

import com.sber_ii_lab.dto.response.NewsDto;
import com.sber_ii_lab.dto.request.NewsRequest;
import com.sber_ii_lab.entity.Author;
import com.sber_ii_lab.entity.News;
import com.sber_ii_lab.entity.Tag;
import com.sber_ii_lab.enums.NewsType;
import com.sber_ii_lab.exception.EntityNotFoundException;
import com.sber_ii_lab.mapper.NewsMapper;
import com.sber_ii_lab.repository.AuthorRepository;
import com.sber_ii_lab.repository.NewsRepository;
import com.sber_ii_lab.repository.TagRepository;
import com.sber_ii_lab.repository.specification.NewsSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class NewsService {
    private final NewsRepository newsRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final NewsMapper newsMapper;
    private final FileStorageService fileStorageService;
    private final TagService tagService;
    private final AuthorService authorService;
    private final ScheduledExecutorService taskScheduler;


    @Transactional
    @PreAuthorize("hasRole('USER')")
    public NewsDto createNews(NewsRequest request) {
        String imageUrl = fileStorageService.storeFile(request.getImage());


        News news = newsMapper.toEntity(request);
        news.setImageUrl(imageUrl);

        Set<Author> authors = request.getAuthorIds().stream()
                .map(authorId -> authorRepository.findById(authorId)
                        .orElseThrow(() -> new EntityNotFoundException("Автор не найден: " + authorId)))
                .collect(Collectors.toSet());

        Set<Tag> tags = request.getTagNames().stream()
                .map(tagName -> tagRepository.findByNameIgnoreCase(tagName)
                        .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build())))
                .collect(Collectors.toSet());

        news.setAuthors(authors);
        news.setTags(tags);

        News savedNews = newsRepository.save(news);
        return newsMapper.toDto(savedNews);
    }

    @Transactional(readOnly = true)
    public List<NewsDto> getAllNews() {
        return newsRepository.findAll()
                .stream()
                .map(newsMapper::toDto)
                .toList();
    }

    @Transactional

    @PreAuthorize("hasRole('USER')")
    public void deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Новость не найдена"));

        String imageUrl = news.getImageUrl();

        newsRepository.delete(news);

        taskScheduler.schedule(() -> {
            tagService.cleanupUnusedTags();
            authorService.cleanupUnusedAuthors();
            fileStorageService.deleteFileIfUnused(imageUrl);
        }, 1, TimeUnit.MINUTES);
    }

    @Transactional
    @PreAuthorize("hasRole('USER')")
    public NewsDto updateNews(Long id, NewsRequest request) {
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Новость не найдена"));

        String newImageUrl = fileStorageService.updateFile(
                request.getImage(),
                existingNews.getImageUrl()
        );

        newsMapper.updateNewsFromRequest(request, existingNews);
        existingNews.setImageUrl(newImageUrl);

        updateAuthors(existingNews, request.getAuthorIds());
        updateTags(existingNews, request.getTagNames());

        return newsMapper.toDto(newsRepository.save(existingNews));
    }


    @PreAuthorize("hasRole('USER')")
    private void updateAuthors(News news, Set<Long> authorIds) {
        List<Author> authors = authorRepository.findAllById(authorIds);
        news.setAuthors(new HashSet<>(authors));
    }


    @PreAuthorize("hasRole('USER')")
    private void updateTags(News news, Set<String> tagNames) {
        Set<Tag> tags = tagNames.stream()
                .map(name -> tagRepository.findByNameIgnoreCase(name)
                        .orElseGet(() -> tagRepository.save(Tag.builder().name(name).build())))
                .collect(Collectors.toSet());
        news.setTags(tags);
    }


    public NewsDto getNewsById(Long id) {
        return newsRepository.findById(id)
                .map(newsMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Новость не найдена"));
    }

    public Page<NewsDto> getNewsPage(int page, int size) {
        return newsRepository.findAll(PageRequest.of(page, size))
                .map(newsMapper::toDto);
    }

    public List<NewsDto> searchNews(String title, NewsType newsType, List<String> tags) {
        Specification<News> spec = NewsSpecifications.combineSpecifications(title, newsType, tags);
        return newsRepository.findAll(spec).stream()
                .map(newsMapper::toDto)
                .toList();
    }
}
