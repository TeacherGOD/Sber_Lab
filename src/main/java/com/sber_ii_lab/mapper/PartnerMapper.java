package com.sber_ii_lab.mapper;

import com.sber_ii_lab.dto.response.PartnerResponseDto;
import com.sber_ii_lab.entity.Partner;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PartnerMapper {

    PartnerResponseDto toDto(Partner partner);

}
