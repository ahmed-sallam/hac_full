package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.inventory.models.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "RFPQLine")
@Table(name = "rfpq_lines")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RFPQLine extends BaseEntity {
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "notes")
    private Integer notes;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
