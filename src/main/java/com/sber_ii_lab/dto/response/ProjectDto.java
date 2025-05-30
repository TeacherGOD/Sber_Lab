package com.sber_ii_lab.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    @Schema(description = "ID проекта", example = "1")
    private Long id;

    @Schema(description = "Название проекта", example = "Интеграция новой системы")
    @NotBlank
    @Size(max = 255)
    private String title;

    @Schema(description = "Описание проекта", example = "Проект по внедрению микросервисной архитектуры")
    @NotBlank
    private String content;

    @Schema(description = "URL изображения", example = "http://localhost:8080/uploads/project_image.jpg")
    private String imageUrl;

    @Schema(description = "Дата создания", example = "2025-04-17 12:00:05")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Set<TagDto> tags;
    private Set<AuthorDto> authors;

    public String getImageUrl() {
        return "http://localhost:8080/uploads/" + this.imageUrl;
    }
}
