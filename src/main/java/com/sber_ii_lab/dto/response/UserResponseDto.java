package com.sber_ii_lab.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Ответ с данными пользователя")
public class UserResponseDto {
    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Логин пользователя", example = "user123")
    private String username;

    @Schema(description = "Роли пользователя", example = "[\"USER\", \"ADMIN\"]")
    private List<String> roles;
}