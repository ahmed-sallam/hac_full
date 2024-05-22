package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "SupplierQuotationExpenses")
@Table(name = "supplier_quotation_expenses")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SupplierQuotationExpenses extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private PurchaseExpensesTitle title;
    private Double amount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_quotation_id")
    private SupplierQuotation supplierQuotation;
}
