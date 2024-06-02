package com.techpeak.hac.purchase.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSupplier implements Serializable {

    Boolean isActive = true;
    @NotEmpty
    String nameAr;
    @NotEmpty
    String nameEn;
    @Email
    String email;
    String phone;
    String address;
}