package com.techpeak.hac.purchase.services.impl;

import com.techpeak.hac.core.dtos.CreateUserHistory;
import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.core.enums.InternalPhase;
import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.repositories.InternalRefRepository;
import com.techpeak.hac.core.services.UserHistoryService;
import com.techpeak.hac.inventory.dtos.StoreResponseShort;
import com.techpeak.hac.inventory.models.Store;
import com.techpeak.hac.purchase.GenerateRequestNumber;
import com.techpeak.hac.purchase.dtos.CreateRFPQ;
import com.techpeak.hac.purchase.dtos.RFPQResponse;
import com.techpeak.hac.purchase.dtos.RFPQResponseShort;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.mappers.RFPQMapper;
import com.techpeak.hac.purchase.models.MaterialRequest;
import com.techpeak.hac.purchase.models.MaterialRequestLine;
import com.techpeak.hac.purchase.models.RFPQ;
import com.techpeak.hac.purchase.models.RFPQLine;
import com.techpeak.hac.purchase.repositories.RFPQRepository;
import com.techpeak.hac.purchase.services.RFPQService;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Override
    public Page<RFPQResponseShort> search(int page, int size, String sort, String search, Long ref, Long store, Long user, String phase, String status) {
        Specification<RFPQ> spec = Specification.where(null);

        if (ref != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("internalRef").get("id"), ref));
        }
        if (store != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("store").get("id"), store));
        }
        if (user != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), user));
        }
        if (phase != null && !phase.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("internalRef").get("currentPhase"), InternalPhase.valueOf(phase.toUpperCase())));
        }
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), RequestStatus.valueOf(status.toUpperCase())));
        }
        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("number"), "%" + search + "%"));
        }

        Pageable pageRequest = PageRequest.of(page, size, Sort.by(sort));
        Page<RFPQ> all = rfpqRepository.findAll(spec, pageRequest);

        return all.map(this::RFPQToResponse);
    }

    @Override
    public RFPQResponse getOne(Long id) {
        Tuple a = rfpqRepository.findByIdWithHistory(id);
        System.out.println("iiiiiii " + a);
        try {
            return RFPQMapper.mapToRFPQResponse(a);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void updateStatus(Long id, RequestStatus status, User user) {
        RFPQ rfpq = getOrElseThrow(id);
        if (status.name().equals(RequestStatus.PROCESSING.name())) {
            if (rfpq.getInternalRef().getCurrentPhase().equals(InternalPhase.REQUEST_FOR_P_QUOTATION))
                rfpq.getInternalRef().setCurrentPhase(InternalPhase.SUPPLIER_QUOTATION);
        }

        rfpq.setStatus(status);
        RFPQ saved = rfpqRepository.save(rfpq);
        String actionDetails = "Update Request for purchase quotation with number: " + saved.getNumber() + " and internal id: " + saved.getInternalRef().getId() + " status >> (" + saved.getStatus().name() + ") ";

        createUserHistory(user, saved, actionDetails);
    }

    public RFPQ getOrElseThrow(Long id) {
        return rfpqRepository.findById(id).orElseThrow(() -> new NotFoundException("RFPQ with id: " + id + " not found"));
    }

    private RFPQResponseShort RFPQToResponse(RFPQ rfpq) {
        return RFPQResponseShort.builder()
                .id(rfpq.getId())
                .number(rfpq.getNumber())
                .date(rfpq.getDate())
                .status(rfpq.getStatus().name())
                .store(new StoreResponseShort(rfpq.getStore().getId(), rfpq.getStore().getNameAr(), rfpq.getStore().getNameEn()))
                .internalRef(rfpq.getInternalRef().getId())
                .user(new UserDtoShort(rfpq.getUser().getId(), rfpq.getUser().getUsername()))
                .currentPhase(rfpq.getInternalRef().getCurrentPhase().name())
                .build();
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
