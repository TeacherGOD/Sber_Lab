package com.sber_ii_lab.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на смену пароля")
public class PasswordUpdateDto {
    @NotBlank
    @Schema(description = "Текущий пароль", example = "oldPass123")
    private String oldPassword;

    @NotBlank
    @Schema(description = "Новый пароль", example = "newPass456")
    private String newPassword;
}
