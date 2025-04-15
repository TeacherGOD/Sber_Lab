package com.sber_ii_lab.service;

import com.sber_ii_lab.entity.Tag;
import com.sber_ii_lab.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {
    private final TagRepository tagRepository;

    @Transactional
    public void cleanupUnusedTags() {
        log.info("Starting tags cleanup...");
        List<Tag> unusedTags = tagRepository.findUnusedTags();
        log.info("Found {} unused tags", unusedTags.size());
        tagRepository.deleteAll(unusedTags);
        log.info("Tags cleanup completed");
    }
}
