package com.sber_ii_lab.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FeedbackRequest {

    @Schema(description = "Оценка отзыва от 1 до 5", example = "4")
    @Min(1)
    @Max(5)
    private int rating;

    @Schema(description = "Текст отзыва", example = "Отличный проект, все работает стабильно")
    @NotBlank
    private String comment;

}
