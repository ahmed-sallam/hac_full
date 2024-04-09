package com.techpeak.hac.inventory.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.techpeak.hac.core.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name="ProductSet")
@Table( name="product_sets")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductSet  extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "product_set_id", nullable = false)
    private Product productSet;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
