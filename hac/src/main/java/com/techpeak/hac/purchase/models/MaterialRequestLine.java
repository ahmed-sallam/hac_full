package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.inventory.models.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "MaterialRequestLine")
@Table(name = "material_request_lines")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MaterialRequestLine extends BaseEntity {
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "notes")
    private Integer notes;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
//    @Column(name = "current_quantity", nullable = false)
//    private Integer currentQuantity;
    // make it later if needed

}
