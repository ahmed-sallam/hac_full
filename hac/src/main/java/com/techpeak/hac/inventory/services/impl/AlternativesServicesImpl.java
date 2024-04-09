package com.techpeak.hac.inventory.services.impl;

import com.techpeak.hac.inventory.dtos.CreateAlternative;
import com.techpeak.hac.inventory.models.Alternative;
import com.techpeak.hac.inventory.repositories.AlternativesRepository;
import com.techpeak.hac.inventory.services.AlternativesServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlternativesServicesImpl implements AlternativesServices {
    private final AlternativesRepository alternativesRepository;
    @Override
    public List<Alternative> list() {
        return alternativesRepository.findAll();
    }

    @Override
    public List<Alternative> listByProductNumber(String productNumber) {
        List<Alternative> alternatives = alternativesRepository.findAllByProductNumberContainingIgnoreCase(productNumber);
        return alternatives;
    }

    @Override
    public void create(CreateAlternative alternative) {
        alternativesRepository.save(Alternative.builder()
                .product1Number(alternative.getProduct1Number())
                .product2Number(alternative.getProduct2Number())
                .build());
    }
}
