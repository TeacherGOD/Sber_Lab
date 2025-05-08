package com.sber_ii_lab.repository;

import com.sber_ii_lab.interfaces.FileReference;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Repository
public class FileReferenceRepository {
    private final EntityManager entityManager;

    public FileReferenceRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public long countFileReferences(String fileName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Получаем все сущности с аннотацией @FileReference
        List<Class<?>> entities = entityManager.getMetamodel()
                .getEntities()
                .stream()
                .filter(e -> hasFileReferenceField(e.getJavaType()))
                .<Class<?>>map(e -> (Class<?>) e.getJavaType())
                .toList();

        // Суммируем результаты для каждой сущности
        long total = 0;
        for (Class<?> entityClass : entities) {
            String entityFieldName = resolveFieldName(entityClass);
            CriteriaQuery<Long> query = cb.createQuery(Long.class);
            Root<?> root = query.from(entityClass);
            query.select(cb.count(root))
                    .where(cb.equal(root.get(entityFieldName), fileName));
            total += entityManager.createQuery(query).getSingleResult();
        }

        return total;
    }

    private String resolveFieldName(Class<?> entityClass) {
        try {
            // Поиск поля с аннотацией @FileReference
            List<Field> fileRefFields = Arrays.stream(entityClass.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(FileReference.class))
                    .toList();

            if (fileRefFields.isEmpty()) {
                throw new IllegalStateException("Сущность " + entityClass.getName() + " не содержит поля с @FileReference");
            }

            if (fileRefFields.size() > 1) {
                throw new IllegalStateException("Сущность " + entityClass.getName() + " содержит несколько полей с @FileReference");
            }

            Field field = fileRefFields.getFirst();
            return field.getName(); // Возвращаем имя поля, а не колонки!

        } catch (Exception e) {
            throw new IllegalStateException("Ошибка в сущности " + entityClass.getName(), e);
        }
    }


    private boolean hasFileReferenceField(Class<?> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .anyMatch(f -> f.isAnnotationPresent(FileReference.class));
    }
}
