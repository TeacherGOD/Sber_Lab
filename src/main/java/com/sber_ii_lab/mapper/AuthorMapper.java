package com.sber_ii_lab.mapper;

import com.sber_ii_lab.dto.response.AuthorDto;
import com.sber_ii_lab.entity.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDto toDto(Author author);

    Author toEntity(AuthorDto authorDto);

}
