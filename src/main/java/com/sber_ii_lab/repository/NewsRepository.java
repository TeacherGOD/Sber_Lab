package com.sber_ii_lab.repository;

import com.sber_ii_lab.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {

    @Query("SELECT DISTINCT n FROM News n " +
            "LEFT JOIN n.tags t " +
            "WHERE (:newsType IS NULL OR n.newsType = :newsType) " +
            "AND (:tagIds IS NULL OR t.id IN :tagIds)")
    Page<News> findByFilters(
            @Param("newsType") String newsType, // Используйте String вместо NewsType
            @Param("tagIds") List<Long> tagIds,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"tags", "authors"})
    Optional<News> findById(Long id);

    @Query("SELECT COUNT(n) FROM News n WHERE n.imageUrl = :fileName")
    long countByImageUrl(@Param("fileName") String fileName);
}