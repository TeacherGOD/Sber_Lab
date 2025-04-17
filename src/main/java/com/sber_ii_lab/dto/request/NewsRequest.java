package com.sber_ii_lab.dto.request;

import com.sber_ii_lab.enums.NewsType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
public class NewsRequest {
    @Schema(description = "Заголовок новости", example = "Новое обновление системы")
    @NotBlank
    private String title;

    @Schema(description = "Заголовок новости", example = "Важное обновление")
    @NotBlank
    private String content;

    @Schema(description = "Тип новости", examples = {"LOCAL","GLOBAL"})
    @NotNull
    private NewsType newsType;

    @Schema(type = "string", format = "binary")
    @Nullable
    private MultipartFile image;

    @Schema(description = "Список Id Авторов", example = "[1,2]")
    @NotEmpty
    private Set<Long> authorIds;

    @NotEmpty
    @Schema(description = "Список имён тегов", example = "[\"важно\", \"срочно\"]")
    private Set<String> tagNames;
}