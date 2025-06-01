package com.sber_ii_lab.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FeedbackRequest {

    @Schema(description = "Email автора отзыва", example = "user@example.com")
    @NotBlank(message = "Email обязателен")
    private String email;

    @Schema(description = "Текст отзыва", example = "Я бы хотел у вас работать")
    @NotBlank
    private String comment;

}
