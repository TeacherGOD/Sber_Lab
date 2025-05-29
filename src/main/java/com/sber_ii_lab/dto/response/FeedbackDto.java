package com.sber_ii_lab.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackDto {

    @Schema(description = "ID отзыва", example = "10")
    private Long id;

    @Schema(description = "Оценка отзыва от 1 до 5", example = "5")
    private int rating;

    @Schema(description = "Текст отзыва", example = "Очень хороший опыт, всем советую!")
    private String comment;

    @Schema(description = "Дата создания", example = "2024-05-22T13:45:00")
    private LocalDateTime createdAt;

}
