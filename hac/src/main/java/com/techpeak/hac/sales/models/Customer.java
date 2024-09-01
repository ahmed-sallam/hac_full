package com.techpeak.hac.sales.models;

import com.techpeak.hac.core.models.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;


@Entity(name = "Customer")
@Table(
        name = "customers",
        indexes = {
                @Index(name = "idx_customers_name_ar", columnList = "name_ar"),
                @Index(name = "idx_customers_name_en", columnList = "name_en")
        }
)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Customer extends BaseEntity {
    @Column(name = "name_ar", nullable = false)
    private String nameAr;
    @Column(name = "name_en", nullable = false)
    private String nameEn;
    private String email;
    private String phone;
    private String address;
}
