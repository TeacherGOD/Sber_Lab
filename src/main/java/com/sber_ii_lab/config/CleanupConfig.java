package com.sber_ii_lab.config;

import com.sber_ii_lab.repository.NewsRepository;
import com.sber_ii_lab.service.AuthorService;
import com.sber_ii_lab.service.FileStorageService;
import com.sber_ii_lab.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class CleanupConfig {

    private final TagService tagService;
    private final AuthorService authorService;
    private final FileStorageService fileStorageService;

    @Scheduled(cron = "0 0 3 * * *") // Каждый день в 3:00
    public void scheduledCleanup() {
        tagService.cleanupUnusedTags();
        authorService.cleanupUnusedAuthors();
        fileStorageService.cleanupUnusedFiles();
    }
}
