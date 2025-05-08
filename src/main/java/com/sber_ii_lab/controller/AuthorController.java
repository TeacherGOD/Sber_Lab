package com.sber_ii_lab.controller;

import com.sber_ii_lab.dto.response.AuthorDto;
import com.sber_ii_lab.entity.Author;
import com.sber_ii_lab.mapper.AuthorMapper;
import com.sber_ii_lab.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(
        name = "Author Management",
        description = "APIs for managing article authors"
)
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    @Operation(
            summary = "Get all authors",
            description = "Returns paginated list of all authors"
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
    @ApiResponse(responseCode = "200", description = "Successfully retrieved authors")
    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    @GetMapping
    public ResponseEntity<Page<AuthorDto>> getAllAuthors(@Parameter(hidden = true) Pageable pageable) {
        Page<Author> authors = authorService.findAll(pageable);
        return ResponseEntity.ok(authors.map(authorMapper::toDto));
    }

    @Operation(
            summary = "Get author by ID",
            description = "Returns a single author by its identifier"
    )
    @ApiResponse(responseCode = "200", description = "Author found")
    @ApiResponse(responseCode = "404", description = "Author not found")
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(
            @Parameter(description = "ID of the author to retrieve", example = "1")
            @PathVariable Long id
    ) {
        return authorService.findById(id)
                .map(authorMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Create new author"
    )
    @ApiResponse(responseCode = "201", description = "Author created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto) {
        Author author = authorMapper.toEntity(authorDto);
        Author savedAuthor = authorService.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(authorMapper.toDto(savedAuthor));
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Update author",
            description = "Updates existing author's name"
    )
    @ApiResponse(responseCode = "200", description = "Author updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "404", description = "Author not found")
    @ApiResponse(responseCode = "409", description = "New name already exists")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(
            @Parameter(description = "ID of the author to update", example = "1")
            @PathVariable Long id,
            @RequestBody AuthorDto authorDto
    ) {
        return authorService.update(id, authorDto)
                .map(authorMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Delete author",
            description = "Deletes an author and removes associations from articles"
    )
    @ApiResponse(responseCode = "204", description = "Author deleted successfully")
    @ApiResponse(responseCode = "404", description = "Author not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @Parameter(description = "ID of the author to delete", example = "1")
            @PathVariable Long id
    ) {
        if (authorService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}