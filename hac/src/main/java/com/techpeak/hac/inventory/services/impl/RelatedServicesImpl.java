package com.techpeak.hac.inventory.services.impl;

import com.techpeak.hac.inventory.dtos.CreateRelated;
import com.techpeak.hac.inventory.models.Related;
import com.techpeak.hac.inventory.repositories.RelatedRepository;
import com.techpeak.hac.inventory.services.RelatedServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatedServicesImpl implements RelatedServices {
    private final RelatedRepository relatedRepository;

    @Override
    public List<Related> list() {
        return relatedRepository.findAll();
    }

    @Override
    public List<Related> listByProductNumber(String productNumber) {
        List<Related> relateds = relatedRepository.findAllByProductNumberContainingIgnoreCase(productNumber);
        return relateds;
    }

    @Override
    public void create(CreateRelated related) {
        relatedRepository.save(Related.builder()
                .product1Number(related.getProduct1Number())
                .product2Number(related.getProduct2Number())
                .isRestricted(related.getIsRestricted())
                .build());
    }
}
