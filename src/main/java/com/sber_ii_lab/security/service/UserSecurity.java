package com.sber_ii_lab.security.service;

import com.sber_ii_lab.entity.User;
import com.sber_ii_lab.repository.UserRepository;
import com.sber_ii_lab.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSecurity {
    private final UserRepository userRepository;

    public boolean isSelf(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof MyUserDetails userDetails) {
            return userDetails.getId().equals(userId);
        }

        throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
    }

        // Если principal - строка (например, логин), проверяем через репозиторий
//        String username = principal.toString();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return user.getId().equals(userId);
//    }
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}