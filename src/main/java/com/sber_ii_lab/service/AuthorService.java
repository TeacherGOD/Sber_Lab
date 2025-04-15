package com.sber_ii_lab.service;

import com.sber_ii_lab.entity.Author;
import com.sber_ii_lab.entity.Tag;
import com.sber_ii_lab.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Transactional
    public void cleanupUnusedAuthors() {
        log.info("Starting author cleanup...");
        List<Author> unusedAuthors = authorRepository.findUnusedAuthors();
        log.info("Found {} unused authors", unusedAuthors.size());
        authorRepository.deleteAll(unusedAuthors);
        log.info("Authors cleanup completed");
    }
}

