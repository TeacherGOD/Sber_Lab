package com.sber_ii_lab.controller;

import com.sber_ii_lab.dto.request.FeedbackRequest;
import com.sber_ii_lab.dto.response.FeedbackDto;
import com.sber_ii_lab.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "Отправить отзыв", description = "Создание нового отзыва")
    @PostMapping
    public ResponseEntity<FeedbackDto> createFeedback(@Valid @RequestBody FeedbackRequest feedbackRequest) {
        FeedbackDto created = feedbackService.createFeedback(feedbackRequest);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Получить все отзывы", description = "Возвращает список всех отзывов")
    @GetMapping
    public ResponseEntity<List<FeedbackDto>> getAllFeedback() {
        List<FeedbackDto> feedbackList = feedbackService.getAllFeedback();
        return ResponseEntity.ok(feedbackList);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удалить отзыв", description = "Удаляет отзыв по ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Отзыв успешно удалён")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}
