package com.techpeak.hac.purchase.models;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VatTotalQuantity {
    @Column(name = "quantity")
    private Integer quantity = 1;
    @Column(name = "vat")
    private Double vat = 0d;
    @Column(name = "total")
    private Double total = 0d;
}
