package com.techpeak.hac.inventory.services;

import java.util.List;

import com.techpeak.hac.inventory.dtos.CreateRelated;
import com.techpeak.hac.inventory.models.Related;

public interface RelatedServices {

    List<Related> list();

    List<Related> listByProductNumber(String productNumber);

    void create(CreateRelated related);
}
