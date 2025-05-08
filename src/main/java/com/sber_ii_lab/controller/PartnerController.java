package com.sber_ii_lab.controller;


import com.sber_ii_lab.dto.request.PartnerCreateDto;
import com.sber_ii_lab.dto.response.PartnerResponseDto;
import com.sber_ii_lab.service.PartnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
@Tag(name = "Партнеры", description = "Управление партнерами компании")
public class PartnerController {
    private final PartnerService partnerService;

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Создать нового партнера",
            description = "Создание партнера с прикрепленным изображением"
    )
    @ApiResponse(responseCode = "201", description = "Партнер успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные входные данные")
    @ApiResponse(responseCode = "409", description = "Конфликт имен партнеров")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PartnerResponseDto createPartner(@Valid @ModelAttribute  @Parameter(description = "Данные партнера") PartnerCreateDto dto) {
        return partnerService.createPartner(dto);
    }

    @Operation(summary = "Получить всех партнеров")
    @ApiResponse(responseCode = "200", description = "Список всех партнеров")
    @GetMapping
    public List<PartnerResponseDto> getAllPartners() {
        return partnerService.getAllPartners();
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удалить партнера по ID")
    @ApiResponse(responseCode = "204", description = "Партнер успешно удален")
    @ApiResponse(responseCode = "404", description = "Партнер не найден")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePartner(@PathVariable Long id) {
        partnerService.deletePartner(id);
    }
}
