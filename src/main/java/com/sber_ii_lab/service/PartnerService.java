package com.sber_ii_lab.service;
import com.sber_ii_lab.dto.request.PartnerCreateDto;
import com.sber_ii_lab.dto.response.PartnerResponseDto;
import com.sber_ii_lab.entity.Partner;
import com.sber_ii_lab.exception.ConflictException;
import com.sber_ii_lab.exception.EntityNotFoundException;
import com.sber_ii_lab.mapper.PartnerMapper;
import com.sber_ii_lab.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final FileStorageService fileStorageService;
    private final PartnerMapper partnerMapper;

    @PreAuthorize("hasRole('USER')")
    @Transactional
    public PartnerResponseDto createPartner(PartnerCreateDto dto) {
        if (partnerRepository.existsByName(dto.name())) {
            throw new ConflictException("Партнер с таким именем уже существует");
        }

        String imageUrl = null;
        if (dto.image() != null && !dto.image().isEmpty()) {
            imageUrl = fileStorageService.storeFile(dto.image());
        }

        Partner partner = new Partner();
        partner.setName(dto.name());
        partner.setImageUrl(imageUrl);

        return partnerMapper.toDto(partnerRepository.save(partner));
    }

    @Transactional(readOnly = true)
    public List<PartnerResponseDto> getAllPartners() {
        return partnerRepository.findAll().stream()
                .map(partnerMapper::toDto)
                .toList();
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional
    public void deletePartner(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Партнер не найден"));

        partnerRepository.delete(partner);

        if (partner.getImageUrl() != null) {
            fileStorageService.deleteFileIfUnused(partner.getImageUrl());
        }


    }
}
