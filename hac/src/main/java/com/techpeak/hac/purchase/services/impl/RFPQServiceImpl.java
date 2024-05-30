package com.techpeak.hac.purchase.services.impl;

import com.techpeak.hac.core.dtos.CreateUserHistory;
import com.techpeak.hac.core.enums.InternalPhase;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.repositories.InternalRefRepository;
import com.techpeak.hac.core.services.UserHistoryService;
import com.techpeak.hac.inventory.models.Store;
import com.techpeak.hac.purchase.GenerateRequestNumber;
import com.techpeak.hac.purchase.dtos.CreateRFPQ;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.models.MaterialRequest;
import com.techpeak.hac.purchase.models.MaterialRequestLine;
import com.techpeak.hac.purchase.models.RFPQ;
import com.techpeak.hac.purchase.models.RFPQLine;
import com.techpeak.hac.purchase.repositories.RFPQRepository;
import com.techpeak.hac.purchase.services.RFPQService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RFPQServiceImpl implements RFPQService {
    private final RFPQRepository rfpqRepository;
    private final UserHistoryService userHistoryService;
    private final InternalRefRepository internalRefRepository;


    @Override
    public RFPQ create(CreateRFPQ createRFPQ, User user) {
        // todo if can create RFPQ directly without MR

        return null;
    }

    @Override
    @Transactional
    public RFPQ createInternal(MaterialRequest mr, User user) {
        InternalRef internalRef = mr.getInternalRef();
        internalRef.setCurrentPhase(InternalPhase.REQUEST_FOR_P_QUOTATION);
        internalRefRepository.save(internalRef);
        Optional<RFPQ> lastOne = rfpqRepository.findTopByOrderByNumberDesc();
        String rfpqNumber = GenerateRequestNumber.generateRequestNumber("RFPQ", lastOne.map(RFPQ::getNumber).orElse(null));
        RFPQ rfpq = buildRFPQ(user, mr.getStore(), internalRef, rfpqNumber);
        rfpq.setLines(mapToRFPQLines(mr.getLines()));
        RFPQ saved = rfpqRepository.save(rfpq);
        // for user history
        String actionDetails = "Created a new Request for purchase quotation (" + rfpq.getStatus().name() + ") with number:" + rfpq.getNumber() + " and internal id: " + rfpq.getInternalRef().getId();
        createUserHistory(user, saved, actionDetails);
        return null;
    }

    private RFPQ buildRFPQ(User user, Store store, InternalRef internalRef, String rfpqNumber) {
        return RFPQ.builder()
                .store(store)
                .number(rfpqNumber)
                .status(RequestStatus.DRAFT)
                .user(user)
                .internalRef(internalRef)
                .build();
    }

    private Set<RFPQLine> mapToRFPQLines(Set<MaterialRequestLine> lines) {
        return lines.stream()
                .map(
                        l -> RFPQLine.builder()
                                .quantity(l.getQuantity())
                                .product(l.getProduct())
                                .build())
                .collect(Collectors.toSet());
    }

    private void createUserHistory(User user, RFPQ rfpq, String actionDetails) {
        CreateUserHistory createUserHistory = new CreateUserHistory(
                actionDetails,
                "rfpqs",
                rfpq.getId(),
                user
        );
        userHistoryService.create(createUserHistory);
    }
}
