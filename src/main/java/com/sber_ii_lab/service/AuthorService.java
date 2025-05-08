package com.sber_ii_lab.service;

import com.sber_ii_lab.dto.response.AuthorDto;
import com.sber_ii_lab.entity.Author;
import com.sber_ii_lab.repository.AuthorRepository;
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
public class AuthorService {
    private final AuthorRepository authorRepository;

    public Page<Author> findAll(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    public Optional<Author> findById(Long id) {
        return authorRepository.findById(id);
    }

    @PreAuthorize("hasRole('USER')")
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @PreAuthorize("hasRole('USER')")
    public Optional<Author> update(Long id, AuthorDto authorDto) {
        return authorRepository.findById(id)
                .map(existingAuthor -> {
                    existingAuthor.setName(authorDto.getName());
                    return authorRepository.save(existingAuthor);
                });
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional
    public boolean deleteById(Long id) {
        return authorRepository.findById(id)
                .map(author -> {
                    authorRepository.delete(author);
                    return true;
                }).orElse(false);
    }

    @Transactional
    public void cleanupUnusedAuthors() {
        log.info("Starting author cleanup...");
        List<Author> unusedAuthors = authorRepository.findUnusedAuthors();
        log.info("Found {} unused authors", unusedAuthors.size());
        authorRepository.deleteAll(unusedAuthors);
        log.info("Authors cleanup completed");
    }
}

