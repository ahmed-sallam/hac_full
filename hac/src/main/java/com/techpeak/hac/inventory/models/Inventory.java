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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private StoreLocation location;

    @Version
    private Long version;

    @Column(name = "reserved_quantity", nullable = false, columnDefinition = "bigint default 0")
    private Integer reservedQuantity = 0;

    public Inventory(Integer quantity, Product product, Store store) {
        super();
    }

    public Inventory(Integer quantity, Product product, Store store,
                     StoreLocation location) {
        super();
    }
}
