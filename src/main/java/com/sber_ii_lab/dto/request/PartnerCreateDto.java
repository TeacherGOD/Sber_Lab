package com.sber_ii_lab.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Данные для создания партнера")
public record PartnerCreateDto(
        @Schema(description = "Название партнера", example = "ООО Ромашка")
        @NotBlank(message = "Название партнера обязательно")
        String name,

        @Schema(description = "Изображение партнера (JPEG/PNG)")
        MultipartFile image
) {}