package com.techpeak.hac.sales.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.techpeak.hac.sales.models.Customer}
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCustomer implements Serializable {
    Boolean isActive;
    @NotEmpty
    String nameAr;
    @NotEmpty
    String nameEn;
    @Email
    String email;
    String phone;
    String address;
}
