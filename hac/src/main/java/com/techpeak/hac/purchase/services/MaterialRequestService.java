package com.techpeak.hac.purchase.services;

import com.techpeak.hac.purchase.dtos.CreateMaterialRequest;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.models.MaterialRequest;

public interface MaterialRequestService {
    MaterialRequest create(CreateMaterialRequest createMaterialRequest);

    void updateStatus(Long id, RequestStatus status);
}
