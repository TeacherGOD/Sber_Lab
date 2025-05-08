package com.sber_ii_lab.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с данными партнера")
public record PartnerResponseDto(
        @Schema(description = "ID партнера", example = "1")
        Long id,

        @Schema(description = "Название партнера", example = "ООО Ромашка")
        String name,

        @Schema(description = "URL изображения партнера", example = "uploads/abc123.jpg")
        String imageUrl
) {

        public String getImageUrl() {
                return "http://localhost:8080/uploads/" + this.imageUrl;
        }
}
