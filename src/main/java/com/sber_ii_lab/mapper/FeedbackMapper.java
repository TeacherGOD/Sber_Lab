package com.sber_ii_lab.mapper;

import com.sber_ii_lab.dto.request.FeedbackRequest;
import com.sber_ii_lab.dto.response.FeedbackDto;
import com.sber_ii_lab.entity.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    FeedbackDto toDto(Feedback feedback);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Feedback toEntity(FeedbackRequest request);
}
