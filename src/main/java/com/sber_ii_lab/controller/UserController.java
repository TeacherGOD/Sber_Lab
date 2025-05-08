package com.sber_ii_lab.controller;

import com.sber_ii_lab.dto.request.PasswordUpdateDto;
import com.sber_ii_lab.dto.response.UserResponseDto;
import com.sber_ii_lab.enums.Role;
import com.sber_ii_lab.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Management", description = "API для управления пользователями")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // Смена пароля
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Смена пароля",
            description = "Позволяет пользователю или администратору изменить пароль"
    )
    @ApiResponse(responseCode = "200", description = "Пароль успешно изменен")
    @ApiResponse(responseCode = "400", description = "Неверный старый пароль")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    @PatchMapping(value = "/{userId}/password",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updatePassword(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long userId,
            @Valid @ModelAttribute PasswordUpdateDto dto
    ) {
        userService.updatePassword(userId, dto);
    }

    // Смена пароля администратором (без старого пароля)
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Смена пароля администратором",
            description = "Позволяет администратору изменить пароль любого пользователя без проверки старого пароля"
    )
    @ApiResponse(responseCode = "200",description = "Пароль успешно изменен")
    @ApiResponse(responseCode = "400",description = "Некорректный запрос")
    @ApiResponse(responseCode = "403",description = "Доступ запрещен (требуются права администратора)")
    @PatchMapping("/{userId}/password/admin")
    public void adminUpdatePassword(
            @PathVariable Long userId,
            @RequestParam String newPassword
    ) {
        userService.adminUpdatePassword(userId, newPassword);
    }

    // Блокировка/разблокировка (только для админа)
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Блокировка/разблокировка",
            description = "Позволяет администратору заблокировать или разблокировать пользователя"
    )
    @ApiResponse(responseCode = "200", description = "Статус изменен")
    @ApiResponse(responseCode = "409", description = "Нельзя заблокировать себя")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    @PatchMapping("/{userId}/status")
    public void toggleUserStatus(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Новый статус (true - активен, false - заблокирован)", example = "false")
            @RequestParam boolean isEnabled
    ) {
        userService.toggleUserStatus(userId, isEnabled);
    }


    //выдать роль
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Добавить роль у пользователя",
            description = "Позволяет администратору добавить роль пользователю"
    )
    @ApiResponse(responseCode = "200", description = "Роль добавлена")
    @ApiResponse(responseCode = "400", description = "Роль уже назначена")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    @PatchMapping("/{userId}/roles")
    public void addRole(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Роль для добавления", example = "ADMIN")
            @RequestParam Role role
    ) {
        userService.addRole(userId, role);
    }

    //забрать роль
    @SecurityRequirement(name = "JWT")
    @Operation(
            summary = "Удалить роль пользователю",
            description = "Позволяет администратору удалить роль у пользователя"
    )
    @ApiResponse(responseCode = "200", description = "Роль удалена")
    @ApiResponse(responseCode = "400", description = "Роль не назначена")
    @ApiResponse(responseCode = "409", description = "Нельзя удалить роль у себя")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    @DeleteMapping("/{userId}/roles")
    public void removeRole(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Роль для удаления", example = "ADMIN")
            @RequestParam Role role
    ) {
        userService.removeRole(userId, role);
    }

    @Operation(
            summary = "Получить список пользователей",
            description = "Возвращает постраничный список всех пользователей"
    )
    @ApiResponse(responseCode = "200", description = "Успешное получение списка")
    @GetMapping
    public Page<UserResponseDto> getAllUsers(
            @Parameter(description = "Номер страницы (начиная с 1)", example = "1")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        return userService.getAllUsers(page, size);
    }
}
