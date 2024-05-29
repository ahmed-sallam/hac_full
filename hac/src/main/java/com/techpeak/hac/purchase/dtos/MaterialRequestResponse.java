package com.techpeak.hac.purchase.dtos;

import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.core.dtos.UserHistoryResponse;
import com.techpeak.hac.inventory.dtos.StoreResponseShort;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequestResponse implements Serializable {
    private Long id;
    private String number;
    private LocalDate date;
    private String status;
    private String notes;
    private StoreResponseShort store;
    private Long internalRef;
    private String currentPhase;
    private UserDtoShort userDto;
    private List<MaterialRequestLineWithStockDto> lines;
    private List<UserHistoryResponse> history;
}
