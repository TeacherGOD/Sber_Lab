package com.sber_ii_lab.mapper;

import com.sber_ii_lab.dto.response.TagDto;
import com.sber_ii_lab.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface TagMapper {

    TagDto toDto(Tag tag);

    Tag toEntity(TagDto tagDto);

}