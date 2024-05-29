package com.techpeak.hac.purchase.services;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.purchase.dtos.CreateMaterialRequest;
import com.techpeak.hac.purchase.dtos.MaterialRequestResponse;
import com.techpeak.hac.purchase.dtos.MaterialRequestResponseShort;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.models.MaterialRequest;
import org.springframework.data.domain.Page;

public interface MaterialRequestService {
    MaterialRequest create(CreateMaterialRequest createMaterialRequest, User user);

    void updateStatus(Long id, RequestStatus status);

    Page<MaterialRequestResponseShort> search(int page, int size, String sort, String search, Long ref, Long store, Long user, String phase, String status);

    MaterialRequestResponse getOne(Long id);
}
