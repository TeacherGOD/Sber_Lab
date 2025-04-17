package com.sber_ii_lab.controller;

import com.sber_ii_lab.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;


    @Operation(summary = "Загрузка файла", description = "Загружает файл на сервер")
    @ApiResponse(responseCode = "200", description = "Файл успешно загружен")
    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @Parameter(description = "Файл для загрузки", required = true,
                    schema = @Schema(type = "string", format = "binary")
            )
            @RequestPart("file") MultipartFile file
    ) {
        String fileName = fileStorageService.storeFile(file);
        return ResponseEntity.ok("Файл загружен: " + fileName);
    }
}