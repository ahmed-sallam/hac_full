package com.techpeak.hac.purchase.services.impl;

import com.techpeak.hac.purchase.dtos.CreateMaterialRequest;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.models.MaterialRequest;
import com.techpeak.hac.purchase.services.MaterialRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MaterialRequestServiceImpl implements MaterialRequestService {
    @Override
    public MaterialRequest create(CreateMaterialRequest createMaterialRequest) {
        return null;
    }

    @Override
    public void updateStatus(Long id, RequestStatus status) {

    }
}
