package com.sber_ii_lab.repository.specification;

import com.sber_ii_lab.entity.Project;
import com.sber_ii_lab.entity.Tag;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProjectSpecifications {

    public static Specification<Project> hasNameContaining(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Project> hasTags(List<String> tagNames) {
        return (root, query, cb) -> {
            if (tagNames == null || tagNames.isEmpty()) {
                return cb.conjunction();
            }
            List<String> lowerCaseTags = tagNames.stream()
                    .map(String::toLowerCase)
                    .toList();
            Join<Project, Tag> tagJoin = root.join("tags");
            return cb.lower(tagJoin.get("name")).in(lowerCaseTags);
        };
    }

    public static Specification<Project> combineSpecifications(
            String name,
            List<String> tags
    ) {
        return Specification.where(hasNameContaining(name))
                .and(hasTags(tags));
    }
}
