package com.techpeak.hac.purchase.services;

import com.techpeak.hac.core.models.User;
import com.techpeak.hac.purchase.dtos.CreateRFPQ;
import com.techpeak.hac.purchase.models.MaterialRequest;
import com.techpeak.hac.purchase.models.RFPQ;

public interface RFPQService {
    RFPQ create(CreateRFPQ createRFPQ, User user);
    RFPQ createInternal(MaterialRequest mr, User user);
}
