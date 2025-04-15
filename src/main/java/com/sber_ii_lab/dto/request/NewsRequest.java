package com.sber_ii_lab.dto.request;

import com.sber_ii_lab.enums.NewsType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
public class NewsRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private NewsType newsType;

    @Nullable
    private MultipartFile image;

    @NotEmpty
    private Set<Long> authorIds;

    @NotEmpty
    private Set<String> tagNames;
}