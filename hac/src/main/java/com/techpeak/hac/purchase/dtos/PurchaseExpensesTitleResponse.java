package com.techpeak.hac.purchase.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseExpensesTitleResponse implements Serializable {
    Long id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isActive;
    String nameAr;
    String nameEn;
}