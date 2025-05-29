package com.sber_ii_lab.repository;

import com.sber_ii_lab.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);

    @Query(value = """
    SELECT a.* FROM authors a
    WHERE NOT EXISTS (
        SELECT 1 FROM news_authors na WHERE na.author_id = a.id
    )
    AND NOT EXISTS (
        SELECT 1 FROM project_authors pa WHERE pa.author_id = a.id
    )
    """, nativeQuery = true)
    List<Author> findUnusedAuthors();
}