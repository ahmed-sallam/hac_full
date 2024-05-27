package com.techpeak.hac.purchase.services.impl;

import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.core.repositories.InternalRefRepository;
import com.techpeak.hac.inventory.services.ProductService;
import com.techpeak.hac.inventory.services.StoreService;
import com.techpeak.hac.purchase.GenerateRequestNumber;
import com.techpeak.hac.purchase.dtos.CreateMaterialRequest;
import com.techpeak.hac.purchase.enums.RequestStatus;
import com.techpeak.hac.purchase.models.MaterialRequest;
import com.techpeak.hac.purchase.models.MaterialRequestLine;
import com.techpeak.hac.purchase.repositories.MaterialRequestRepository;
import com.techpeak.hac.purchase.services.MaterialRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final InternalRefRepository internalRefRepository;

    @Override
    @Transactional
    public MaterialRequest create(CreateMaterialRequest createMaterialRequest, User user) throws RuntimeException{
        InternalRef internalRef = new InternalRef();
//        internalRefRepository.save(internalRef);
        Optional<MaterialRequest> lastOne = materialRequestRepository.findTopByOrderByNumberDesc();
        String materialRequestNumber = GenerateRequestNumber.generateRequestNumber("MR", lastOne.map(MaterialRequest::getNumber).orElse(null));
        MaterialRequest materialRequest = buildMaterialRequest(createMaterialRequest, materialRequestNumber,  user, internalRef);
        Set<MaterialRequestLine> lines = mapToMaterialRequestLines(createMaterialRequest);
        System.out.println("llllllll "+ lines);
        materialRequest.setLines(lines);
        return materialRequestRepository.save(materialRequest);
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
                .status(RequestStatus.DRAFT)
                .user(user)
                .internalRef(internalRef)
                .build();
    }

    private Set<MaterialRequestLine> mapToMaterialRequestLines(CreateMaterialRequest createMaterialRequest) {
        return createMaterialRequest
                .getLines().stream().map(l ->
                        {
                            log.info("llllllll " + l.getProduct() );
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
    public void updateStatus(Long id, RequestStatus status) {

    }
}
