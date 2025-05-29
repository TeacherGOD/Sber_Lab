package com.sber_ii_lab.service;

import com.sber_ii_lab.dto.request.PasswordUpdateDto;
import com.sber_ii_lab.dto.request.UserCreateDto;
import com.sber_ii_lab.dto.response.UserResponseDto;
import com.sber_ii_lab.entity.User;
import com.sber_ii_lab.enums.Role;
import com.sber_ii_lab.exception.BadRequestException;
import com.sber_ii_lab.exception.ConflictException;
import com.sber_ii_lab.exception.NotFoundException;
import com.sber_ii_lab.mapper.UserMapper;
import com.sber_ii_lab.repository.UserRepository;
import com.sber_ii_lab.security.service.UserSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserSecurity userSecurity;
    private final SessionRegistry sessionRegistry;


    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(userMapper::toResponseDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        if (userRepository.existsByUsername(userCreateDto.getUsername())) {
            throw new ConflictException("Username already exists");
        }

        User user = userMapper.toEntity(userCreateDto);
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setRoles(Set.of(Role.USER));
        user.setEnabled(true);

        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isSelf(#userId)")
    public void updatePassword(
            Long userId,
            PasswordUpdateDto dto
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void adminUpdatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void toggleUserStatus(Long userId, boolean isEnabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (userSecurity.isSelf(userId)) {
            throw new ConflictException("Нельзя заблокировать самого себя");
        }

        user.setEnabled(isEnabled);

        if (!isEnabled) {
            // Завершаем все активные сессии пользователя
            expireUserSessions(user.getUsername());
        }
    }
    private void expireUserSessions(String username) {
        List<Object> principals = sessionRegistry.getAllPrincipals()
                .stream()
                .filter(p -> ((UserDetails) p).getUsername().equals(username))
                .toList();

        for (Object principal : principals) {
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
            for (SessionInformation session : sessions) {
                session.expireNow(); // Завершаем сессию
            }
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void addRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!Arrays.asList(Role.values()).contains(role)) {
            throw new BadRequestException("Invalid role: " + role);
        }

        if (user.getRoles().contains(role)) {
            throw new ConflictException("Role already assigned");
        }

        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void removeRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getRoles().contains(role)) {
            throw new ConflictException("Role not assigned");
        }

        if (user.getRoles().size() == 1) {
            throw new BadRequestException("Cannot remove the last role");
        }

        if (userSecurity.isSelf(userId)) {
            throw new ConflictException("Нельзя забрать роль у самого себя");
        }

        user.getRoles().remove(role);
        userRepository.save(user);

        if (role == Role.ADMIN) {
            expireUserSessions(user.getUsername());
        }
    }
}
