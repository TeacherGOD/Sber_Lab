package com.sber_ii_lab.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@Schema(description = "Запрос на создание пользователя")
public class UserCreateDto {
    @NotBlank
    @Schema(description = "Логин пользователя", example = "user123")
    private String username;

    @NotBlank
    @Schema(description = "Пароль пользователя", example = "mySecurePassword")
    private String password;

}