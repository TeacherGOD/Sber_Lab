package com.sber_ii_lab.service;

import com.sber_ii_lab.dto.response.TagDto;
import com.sber_ii_lab.entity.Tag;
import com.sber_ii_lab.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {
    private final TagRepository tagRepository;

    public Page<Tag> findAll(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    public Optional<Tag> findById(Long id) {
        return tagRepository.findById(id);
    }

    @PreAuthorize("hasRole('USER')")
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    @PreAuthorize("hasRole('USER')")
    public Optional<Tag> update(Long id, TagDto tagDto) {
        return tagRepository.findById(id)
                .map(existingTag -> {
                    existingTag.setName(tagDto.getName());
                    return tagRepository.save(existingTag);
                });
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional
    public boolean deleteById(Long id) {
        return tagRepository.findById(id)
                .map(tag -> {
                    tagRepository.delete(tag);
                    return true;
                }).orElse(false);
    }

    @Transactional
    public void cleanupUnusedTags() {
        log.info("Starting tags cleanup...");
        List<Tag> unusedTags = tagRepository.findUnusedTags();
        log.info("Found {} unused tags", unusedTags.size());
        tagRepository.deleteAll(unusedTags);
        log.info("Tags cleanup completed");
    }
}
