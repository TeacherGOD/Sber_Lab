package com.sber_ii_lab.config;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class FileValidator {
    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png");

    public void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ValidationException("Файл не может быть пустым");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new ValidationException("Недопустимый тип файла");
        }
    }
}