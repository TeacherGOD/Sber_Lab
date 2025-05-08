package com.sber_ii_lab.controller;

import com.sber_ii_lab.dto.response.TagDto;
import com.sber_ii_lab.entity.Tag;
import com.sber_ii_lab.mapper.TagMapper;
import com.sber_ii_lab.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag Management", description = "APIs for managing article tags")
public class TagController {
    private final TagService tagService;
    private final TagMapper tagMapper;


    @Operation(
            summary = "Get all tags",
            description = "Returns paginated list of all tags"
    )
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
            example = "name,asc",
            schema = @Schema(type = "string")
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved tags")
    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    @GetMapping
    public ResponseEntity<Page<TagDto>> getAllTags(@Parameter(hidden = true) Pageable pageable) {
        Page<Tag> tags = tagService.findAll(pageable);
        return ResponseEntity.ok(tags.map(tagMapper::toDto));
    }

    @Operation(
            summary = "Get tag by ID",
            description = "Returns a single tag by its identifier"
    )
    @ApiResponse(responseCode = "200", description = "Tag found")
    @ApiResponse(responseCode = "404", description = "Tag not found")
    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTagById(
            @Parameter(description = "ID of the tag to retrieve", example = "1")
            @PathVariable Long id
    ) {
        return tagService.findById(id)
                .map(tagMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Create new tag",
            description = "Creates a new tag with unique name",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"name\": \"breaking-news\"}"
                            ),
                            schema = @Schema(implementation = TagDto.class)
                    )
            )
    )
    @ApiResponse(responseCode = "201", description = "Tag created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "409", description = "Tag name already exists")
    @PostMapping
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto) {
        Tag tag = tagMapper.toEntity(tagDto);
        Tag savedTag = tagService.save(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(tagMapper.toDto(savedTag));
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Update tag",
            description = "Updates existing tag's name"
    )
    @ApiResponse(responseCode = "200", description = "Tag updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Tag not found")
    @ApiResponse(responseCode = "409", description = "New name already exists")
    @PutMapping("/{id}")
    public ResponseEntity<TagDto> updateTag(
            @Parameter(description = "ID of the tag to retrieve", example = "1")
            @PathVariable Long id,
            @RequestBody TagDto tagDto
    ) {
        return tagService.update(id, tagDto)
                .map(tagMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Delete tag",
            description = "Deletes a tag and removes it from all associated articles"
    )
    @ApiResponse(responseCode = "204", description = "Tag deleted successfully")
    @ApiResponse(responseCode = "404", description = "Tag not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(
            @Parameter(description = "ID of the tag to retrieve", example = "1")
            @PathVariable Long id
    ) {
        if (tagService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
