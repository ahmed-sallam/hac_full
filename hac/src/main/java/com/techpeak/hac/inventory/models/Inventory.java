package com.techpeak.hac.inventory.models;

import com.techpeak.hac.core.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Inventory")
@Table(name = "inventory",
        uniqueConstraints =
                {
                        @UniqueConstraint(
                                columnNames = {"product_id", "store_id", "location_id"}
                        )
                }
)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Inventory extends BaseEntity {
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private StoreLocation location;

    public Inventory(Integer quantity, Product product, Store store) {
        super();
    }
}
