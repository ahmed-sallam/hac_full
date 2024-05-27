package com.techpeak.hac.purchase.services;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.purchase.dtos.CreateMaterialRequest;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.models.MaterialRequest;

public interface MaterialRequestService {
    MaterialRequest create(CreateMaterialRequest createMaterialRequest, User user);

    void updateStatus(Long id, RequestStatus status);
}
