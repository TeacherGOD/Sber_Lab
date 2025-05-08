package com.sber_ii_lab.repository;

import com.sber_ii_lab.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    boolean existsByName(String name);
}
