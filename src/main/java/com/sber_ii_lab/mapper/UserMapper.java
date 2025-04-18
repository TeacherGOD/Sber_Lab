package com.sber_ii_lab.mapper;


import com.sber_ii_lab.dto.request.UserCreateDto;
import com.sber_ii_lab.dto.response.UserResponseDto;
import com.sber_ii_lab.entity.User;
import com.sber_ii_lab.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User toEntity(UserCreateDto userCreateDto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStrings")
    UserResponseDto toResponseDto(User user);

    default Page<UserResponseDto> toPageResponseDto(Page<User> users) {
        return users.map(this::toResponseDto);
    }


    @Named("rolesToStrings")
    default List<String> rolesToStrings(Set<Role> roles) {
        return roles.stream()
                .map(Role::name)
                .toList();
    }


}