package com.techpeak.hac.purchase.dtos;

import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.inventory.dtos.StoreResponseShort;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequestResponseShort implements Serializable {
    private Long id;
    private String number;
    private LocalDate date;
    private String status;
    private StoreResponseShort store;
    private Long internalRef;
    private UserDtoShort user;
    private String currentPhase;
}
