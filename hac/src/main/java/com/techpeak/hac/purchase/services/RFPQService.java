package com.techpeak.hac.purchase.services;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.purchase.dtos.CreateRFPQ;
import com.techpeak.hac.purchase.dtos.RFPQResponseShort;
import com.techpeak.hac.purchase.models.MaterialRequest;
import com.techpeak.hac.purchase.models.RFPQ;
import org.springframework.data.domain.Page;

public interface RFPQService {
    RFPQ create(CreateRFPQ createRFPQ, User user);
    RFPQ createInternal(MaterialRequest mr, User user);

    Page<RFPQResponseShort> search(int page, int size, String sort, String search, Long ref, Long store, Long user, String phase, String status);
}
