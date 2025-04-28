package com.sber_ii_lab.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на аутентификацию")
public class LoginRequest {
    @NotBlank
    @Schema(description = "Логин", example = "user123")
    private String username;

    @NotBlank
    @Schema(description = "Пароль", example = "secret")
    private String password;
}
