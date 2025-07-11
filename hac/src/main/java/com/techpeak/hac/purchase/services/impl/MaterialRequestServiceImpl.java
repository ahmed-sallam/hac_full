package com.techpeak.hac.purchase.services.impl;

import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.core.enums.InternalPhase;
import com.techpeak.hac.core.exception.NotFoundException;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.services.UserHistoryService;
import com.techpeak.hac.inventory.dtos.StoreResponseShort;
import com.techpeak.hac.inventory.services.ProductService;
import com.techpeak.hac.inventory.services.StoreService;
import com.techpeak.hac.purchase.GenerateRequestNumber;
import com.techpeak.hac.purchase.dtos.CreateMaterialRequest;
import com.techpeak.hac.purchase.dtos.MaterialRequestResponse;
import com.techpeak.hac.purchase.dtos.MaterialRequestResponseShort;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.mappers.MaterialRequestMapper;
import com.techpeak.hac.purchase.models.MaterialRequest;
import com.techpeak.hac.purchase.models.MaterialRequestLine;
import com.techpeak.hac.purchase.repositories.MaterialRequestRepository;
import com.techpeak.hac.purchase.services.MaterialRequestService;
import com.techpeak.hac.purchase.services.RFPQService;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MaterialRequestServiceImpl implements MaterialRequestService {
    private final MaterialRequestRepository materialRequestRepository;
    private final StoreService storeService;
    private final ProductService productService;
    private final UserHistoryService userHistoryService;
    private final RFPQService rfpqService;

    @Override
    @Transactional
    public MaterialRequest create(CreateMaterialRequest createMaterialRequest, User user) throws RuntimeException {
        InternalRef internalRef = new InternalRef();
        internalRef.setCurrentPhase(InternalPhase.MATERIAL_REQUEST);
        Optional<MaterialRequest> lastOne = materialRequestRepository.findTopByOrderByNumberDesc();
        String materialRequestNumber = GenerateRequestNumber.generateRequestNumber("MR", lastOne.map(MaterialRequest::getNumber).orElse(null));
        MaterialRequest materialRequest = buildMaterialRequest(createMaterialRequest, materialRequestNumber, user, internalRef);
        Set<MaterialRequestLine> lines = mapToMaterialRequestLines(createMaterialRequest);
        materialRequest.setLines(lines);
        MaterialRequest savedMaterialRequest = materialRequestRepository.save(materialRequest);
        // for user history
        String actionDetails = "Created a new Material Request (" + savedMaterialRequest.getStatus().name() + ") with number:" + savedMaterialRequest.getNumber() + " and internal id: " + savedMaterialRequest.getInternalRef().getId();
        userHistoryService.createUserHistory(user, savedMaterialRequest.getId(), actionDetails, "material_requests");
        return savedMaterialRequest;
    }

    private MaterialRequest buildMaterialRequest(
            CreateMaterialRequest createMaterialRequest,
            String materialRequestNumber,
            User user, InternalRef internalRef) {
        return MaterialRequest.builder()
                .notes(createMaterialRequest.getNotes())
                .date(createMaterialRequest.getDate())
                .store(storeService.getOrElseThrow(createMaterialRequest.getStore()))
                .number(materialRequestNumber)
                .status(createMaterialRequest.getStatus())
                .user(user)
                .internalRef(internalRef)
                .build();
    }

    private Set<MaterialRequestLine> mapToMaterialRequestLines(CreateMaterialRequest createMaterialRequest) {
        return createMaterialRequest
                .getLines().stream().map(l ->
                        {
                            try {
                                return MaterialRequestLine.builder()
                                        .quantity(l.getQuantity())
                                        .notes(l.getNotes())
                                        .product(productService.getProductOrThrow(l.getProduct()))
                                        .build();
                            } catch (Exception e) {
                                log.error("Error fetching product with id: " + l.getProduct(), e);
                                throw e;
                            }
                        }
                ).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void updateStatus(Long id, RequestStatus status, User user) {
        MaterialRequest mr = getOrElseThrow(id);

        if (status.name().equals(RequestStatus.PROCESSING.name())) {
            rfpqService.createInternal(mr, user);
        }
        mr.setStatus(status);
        MaterialRequest saved = materialRequestRepository.save(mr);
        String actionDetails = "Update Material Request with number: " + saved.getNumber() + " and internal id: " + saved.getInternalRef().getId() + " status >> (" + saved.getStatus().name() + ") ";
        userHistoryService.createUserHistory(user, saved.getId(), actionDetails, "material_requests");
    }

    private MaterialRequest getOrElseThrow(Long id) {
        return materialRequestRepository.findById(id).orElseThrow(() -> new NotFoundException("Material Request with id: " + id + " not found"));
    }

    @Override
    public Page<MaterialRequestResponseShort> search(int page, int size, String sort, String search, Long ref, Long store, Long user, String phase, String status) {
        Specification<MaterialRequest> spec = Specification.where(null);

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
        Page<MaterialRequest> all = materialRequestRepository.findAll(spec, pageRequest);

        return all.map(this::MaterialRequestToResponse);
    }

    @Override
    public MaterialRequestResponse getOne(Long id) {
        Tuple byIdWithStock = materialRequestRepository.findByIdWithStock(id, 2l);
        try {
            return MaterialRequestMapper.mapToMaterialRequestResponse(byIdWithStock);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private MaterialRequestResponseShort MaterialRequestToResponse(MaterialRequest materialRequest) {
        return MaterialRequestResponseShort.builder()
                .id(materialRequest.getId())
                .number(materialRequest.getNumber())
                .date(materialRequest.getDate())
                .status(materialRequest.getStatus().name())
                .store(new StoreResponseShort(materialRequest.getStore().getId(), materialRequest.getStore().getNameAr(), materialRequest.getStore().getNameEn()))
                .internalRef(materialRequest.getInternalRef().getId())
                .user(new UserDtoShort(materialRequest.getUser().getId(), materialRequest.getUser().getUsername()))
                .currentPhase(materialRequest.getInternalRef().getCurrentPhase().name())
                .build();
    }

}
