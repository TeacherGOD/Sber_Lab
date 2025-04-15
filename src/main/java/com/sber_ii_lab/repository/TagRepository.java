package com.sber_ii_lab.repository;

import com.sber_ii_lab.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    Optional<Tag> findByNameIgnoreCase(String name);

    @Query(value = """
        SELECT t.* FROM tags t
        WHERE NOT EXISTS (
            SELECT 1 FROM news_tags nt 
            WHERE nt.tag_id = t.id
        )
        """, nativeQuery = true)
    List<Tag> findUnusedTags();

}
