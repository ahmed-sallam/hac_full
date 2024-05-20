package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.inventory.models.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity(name = "MaterialRequest")
@Table(name = "material_requests")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MaterialRequest extends BaseEntity {
    private String number;
    private String notes;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name="store_id" , nullable = false)
    private Store store;
}
