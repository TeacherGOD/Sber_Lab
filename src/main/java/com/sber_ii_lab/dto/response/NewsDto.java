package com.sber_ii_lab.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sber_ii_lab.enums.NewsType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private String  newsType;

    private String imageUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Set<TagDto> tags;
    private Set<AuthorDto> authors;


    public String getImageUrl() {
        return "http://localhost:8080/uploads/" + this.imageUrl;
    }
}
