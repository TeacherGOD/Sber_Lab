package com.sber_ii_lab.mapper;


import com.sber_ii_lab.dto.response.NewsDto;
import com.sber_ii_lab.dto.request.NewsRequest;
import com.sber_ii_lab.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        uses = {AuthorMapper.class, TagMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NewsMapper {

    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "newsType.displayName", target = "newsType")
    NewsDto toDto(News news);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "authors", ignore = true)
    News toEntity(NewsRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    void updateNewsFromRequest(NewsRequest request, @MappingTarget News news);
}