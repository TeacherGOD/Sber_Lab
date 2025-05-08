package com.sber_ii_lab.repository.specification;

import com.sber_ii_lab.entity.News;
import com.sber_ii_lab.entity.Tag;
import com.sber_ii_lab.enums.NewsType;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class NewsSpecifications {

    public static Specification<News> hasTitleContaining(String title) {
        return (root, query, cb) -> {
            if (title == null || title.isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<News> hasNewsType(NewsType newsType) {
        return (root, query, cb) -> {
            if (newsType == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("newsType"), newsType);
        };
    }

    public static Specification<News> hasTags(List<String> tagNames) {
        return (root, query, cb) -> {
            if (tagNames == null || tagNames.isEmpty()) {
                return cb.conjunction();
            }
            List<String> lowerCaseTags = tagNames.stream()
                    .map(String::toLowerCase)
                    .toList();
            Join<News, Tag> tagJoin = root.join("tags");
            return cb.lower(tagJoin.get("name")).in(lowerCaseTags);
        };
    }

    public static Specification<News> combineSpecifications(
            String title,
            NewsType newsType,
            List<String> tags
    ) {
        return Specification.where(hasTitleContaining(title))
                .and(hasNewsType(newsType))
                .and(hasTags(tags));
    }

    private NewsSpecifications() {
        throw new IllegalArgumentException("util class");
    }
}
