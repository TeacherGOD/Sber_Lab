package com.sber_ii_lab.controller;


import com.sber_ii_lab.dto.request.NewsRequest;
import com.sber_ii_lab.dto.response.NewsDto;
import com.sber_ii_lab.enums.NewsType;
import com.sber_ii_lab.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    // Получить все новости
    @GetMapping
    public ResponseEntity<List<NewsDto>> getAllNews() {
        return ResponseEntity.ok(newsService.getAllNews());
    }

    // Получить новость по ID
    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> getNewsById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }

    // Получить новости с пагинацией
    @GetMapping("/page")
    public ResponseEntity<Page<NewsDto>> getNewsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(newsService.getNewsPage(page, size));
    }
    // Поиск новостей по фильтрам
    @GetMapping("/search")
    public ResponseEntity<List<NewsDto>> searchNews(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) NewsType newsType,
            @RequestParam(required = false) List<String> tags) {
        return ResponseEntity.ok(newsService.searchNews(title, newsType, tags));
    }

    // Создать новость
    //todo права
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NewsDto> createNews(
            @Valid @ModelAttribute NewsRequest request
    ) {
        NewsDto createdNews = newsService.createNews(request);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
    }


    // Удалить новость
    //todo права
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    // Обновить новость (опционально)
    //todo права
    @PutMapping("/{id}")
    public ResponseEntity<NewsDto> updateNews(
            @PathVariable Long id,
            @Valid @ModelAttribute NewsRequest request
    ) {
        return ResponseEntity.ok(newsService.updateNews(id, request));
    }
}