package com.techpeak.hac.inventory.services;

import com.techpeak.hac.inventory.dtos.CreateAlternative;
import com.techpeak.hac.inventory.dtos.CreateRelated;
import com.techpeak.hac.inventory.models.Alternative;
import com.techpeak.hac.inventory.models.Related;

import java.util.List;

public interface RelatedServices {

    List<Related> list();
    List<Related> listByProductNumber(String productNumber);
    void create(CreateRelated related);
}
