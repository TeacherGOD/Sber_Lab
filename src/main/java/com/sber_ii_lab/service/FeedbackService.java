package com.sber_ii_lab.service;

import com.sber_ii_lab.dto.request.FeedbackRequest;
import com.sber_ii_lab.dto.response.FeedbackDto;
import com.sber_ii_lab.entity.Feedback;
import com.sber_ii_lab.exception.EntityNotFoundException;
import com.sber_ii_lab.mapper.FeedbackMapper;
import com.sber_ii_lab.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    @Transactional
    public FeedbackDto createFeedback(FeedbackRequest request) {
        log.info("Создание нового отзыва от пользователя с почтой: {}", request.getEmail());

        Feedback feedback = feedbackMapper.toEntity(request);

        Feedback saved = feedbackRepository.save(feedback);
        return feedbackMapper.toDto(saved);
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional(readOnly = true)
    public List<FeedbackDto> getAllFeedback() {
        return feedbackRepository.findAll()
                .stream()
                .map(feedbackMapper::toDto)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteFeedback(Long id) {
        log.warn("Удаление отзыва с ID: {}", id);

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Отзыв не найден: " + id));

        feedbackRepository.delete(feedback);
    }


}
