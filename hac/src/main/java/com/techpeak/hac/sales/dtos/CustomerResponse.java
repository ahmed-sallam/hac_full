package com.techpeak.hac.sales.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.techpeak.hac.sales.models.Customer}
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse implements Serializable {
    Long id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isActive;
    String nameAr;
    String nameEn;
    String email;
    String phone;
    String address;
}
