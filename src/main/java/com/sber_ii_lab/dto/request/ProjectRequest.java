package com.sber_ii_lab.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
public class ProjectRequest {

    @Schema(description = "Название проекта", example = "Интеграция новой системы")
    @NotBlank
    private String title;

    @Schema(description = "Описание проекта", example = "Проект по внедрению микросервисной архитектуры")
    @NotBlank
    private String content;

    @Schema(type = "string", format = "binary", description = "Изображение проекта (опционально)")
    @Nullable
    private MultipartFile image;

    @Schema(description = "Список Id авторов проекта", example = "[1,2]")
    @NotEmpty
    private Set<Long> authorIds;

    @Schema(description = "Список имён тегов", example = "[\"внедрение\", \"архитектура\"]")
    @NotEmpty
    private Set<String> tagNames;
}
