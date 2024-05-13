package com.techpeak.hac.inventory.services;

import com.techpeak.hac.inventory.dtos.CreateMachineryModel;
import com.techpeak.hac.inventory.dtos.MachineryModelResponse;
import com.techpeak.hac.inventory.models.MachineryModel;

import java.util.List;

public interface MachineryModelService {
    void create(Long machineryTypeId,  CreateMachineryModel createMachineryModel);
    void update(Long id, CreateMachineryModel createMachineryModel);
    List<MachineryModelResponse> list();
     MachineryModel getOrElseThrow(Long id);
}
