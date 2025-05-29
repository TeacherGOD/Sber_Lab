package com.sber_ii_lab.controller;

import com.sber_ii_lab.dto.request.ProjectRequest;
import com.sber_ii_lab.dto.response.ProjectDto;
import com.sber_ii_lab.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @Operation(summary = "Получить все проекты", description = "Возвращает список всех проектов")
    @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @Operation(summary = "Получить проект по ID", description = "Возвращает проект по указанному идентификатору")
    @ApiResponse(responseCode = "200", description = "Проект найден")
    @ApiResponse(responseCode = "404", description = "Проект не найден")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(
            @Parameter(description = "ID проекта", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }


    @Parameter(
            name = "page",
            description = "Номер страницы (начиная с 1)",
            example = "1",
            schema = @Schema(type = "integer", defaultValue = "1")
    )
    @Parameter(
            name = "size",
            description = "Количество элементов на странице",
            example = "20",
            schema = @Schema(type = "integer", defaultValue = "20")
    )
    @Parameter(
            name = "sort",
            description = "Поле для сортировки (формат: поле,напр.asc)",
            example = "createdAt,asc",
            schema = @Schema(type = "string")
    )
    @Operation(summary = "Постраничный вывод проектов", description = "Возвращает проекты с пагинацией")
    @ApiResponse(responseCode = "200", description = "Успешное получение страницы")
    @GetMapping("/page")
    public ResponseEntity<Page<ProjectDto>> getProjectsPage(
            @Parameter(hidden = true) Pageable pageable
    ) {
        return ResponseEntity.ok(projectService.getProjectsPage(pageable));
    }

    @Operation(summary = "Поиск проектов", description = "Фильтрация по названию и тегам")
    @ApiResponse(responseCode = "200", description = "Успешный поиск")
    @GetMapping("/search")
    public ResponseEntity<List<ProjectDto>> searchProjects(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> tags
    ) {
        return ResponseEntity.ok(projectService.searchProjects(name, tags));
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Создать проект", description = "Создание проекта с изображением и описанием")
    @ApiResponse(responseCode = "201", description = "Проект успешно создан")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectDto> createProject(
            @Valid @ModelAttribute ProjectRequest request
    ) {
        ProjectDto createdProject = projectService.createProject(request);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Удалить проект", description = "Удаление проекта по ID")
    @ApiResponse(responseCode = "204", description = "Проект удалён")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Обновить проект", description = "Обновление данных проекта")
    @ApiResponse(responseCode = "200", description = "Проект обновлён")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable Long id,
            @Valid @ModelAttribute ProjectRequest request
    ) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }
}
