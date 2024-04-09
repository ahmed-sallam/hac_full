package com.techpeak.hac.inventory.services;

import com.techpeak.hac.inventory.dtos.CreateAlternative;
import com.techpeak.hac.inventory.models.Alternative;

import java.util.List;

public interface AlternativesServices {

    List<Alternative> list();
    List<Alternative> listByProductNumber(String productNumber);
    void create(CreateAlternative alternative);
}
