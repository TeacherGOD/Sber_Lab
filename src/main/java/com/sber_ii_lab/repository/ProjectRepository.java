package com.sber_ii_lab.repository;

import com.sber_ii_lab.entity.Project;
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
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN p.tags t " +
            "WHERE (:tagIds IS NULL OR t.id IN :tagIds)")
    Page<Project> findByFilters(
            @Param("tagIds") List<Long> tagIds,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"tags", "authors"})
    Optional<Project> findById(Long id);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.imageUrl = :fileName")
    long countByImageUrl(@Param("fileName") String fileName);
}
