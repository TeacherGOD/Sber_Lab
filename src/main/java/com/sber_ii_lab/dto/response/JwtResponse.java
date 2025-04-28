package com.sber_ii_lab.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Ответ с JWT-токеном")
public class JwtResponse {
    @Schema(description = "JWT-токен", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }
}