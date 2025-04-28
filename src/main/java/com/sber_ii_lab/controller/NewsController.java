package com.sber_ii_lab.controller;


import com.sber_ii_lab.dto.request.NewsRequest;
import com.sber_ii_lab.dto.response.NewsDto;
import com.sber_ii_lab.enums.NewsType;
import com.sber_ii_lab.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(summary = "Получить все новости", description = "Возвращает список всех новостей")
    @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    @GetMapping
    public ResponseEntity<List<NewsDto>> getAllNews() {
        return ResponseEntity.ok(newsService.getAllNews());
    }

    // Получить новость по ID
    @Operation(summary = "Получить новость по ID", description = "Возвращает новость по указанному идентификатору")
    @ApiResponse(responseCode = "200", description = "Новость найдена")
    @ApiResponse(responseCode = "404", description = "Новость не найдена")
    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> getNewsById(
            @Parameter(description = "ID новости", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }

    // Получить новости с пагинацией
    @Operation(summary = "Постраничный вывод новостей", description = "Возвращает новости с пагинацией")
    @ApiResponse(responseCode = "200", description = "Успешное получение страницы")
    @GetMapping("/page")
    public ResponseEntity<Page<NewsDto>> getNewsPage(
            @Parameter(description = "Номер страницы (с 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(newsService.getNewsPage(page, size));
    }
    // Поиск новостей по фильтрам
    @Operation(summary = "Поиск новостей", description = "Фильтрация по заголовку, типу и тегам")
    @ApiResponse(responseCode = "200", description = "Успешный поиск")
    @GetMapping("/search")
    public ResponseEntity<List<NewsDto>> searchNews(
            @Parameter(description = "Часть заголовка", example = "Важное")
            @RequestParam(required = false) String title,
            @Parameter(description = "Тип новости", example = "LOCAL")
            @RequestParam(required = false) NewsType newsType,
            @Parameter(description = "Список тегов", example = "[\"важно\", \"срочно\"]")
            @RequestParam(required = false) List<String> tags
    ) {
        return ResponseEntity.ok(newsService.searchNews(title, newsType, tags));
    }

    // Создать новость
    //todo права
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Создать новость", description = "Создание новости с изображением")
    @ApiResponse(responseCode = "201", description = "Новость успешно создана")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NewsDto> createNews(
            @Valid @ModelAttribute NewsRequest request
    ) {
        NewsDto createdNews = newsService.createNews(request);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
    }


    // Удалить новость
    //todo права
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удалить новость", description = "Удаление новости по ID")
    @ApiResponse(responseCode = "204", description = "Новость удалена")
    @ApiResponse(responseCode = "404", description = "Новость не найдена")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(
            @Parameter(description = "ID новости", example = "1")
            @PathVariable Long id
    ) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    // Обновить новость (опционально)
    //todo права
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Обновить новость", description = "Обновление данных новости")
    @ApiResponse(responseCode = "200", description = "Новость обновлена")
    @ApiResponse(responseCode = "404", description = "Новость не найдена")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NewsDto> updateNews(
            @Parameter(description = "ID новости", example = "1")
            @PathVariable Long id,
            @Valid @ModelAttribute NewsRequest request
    ) {
        return ResponseEntity.ok(newsService.updateNews(id, request));
    }
}