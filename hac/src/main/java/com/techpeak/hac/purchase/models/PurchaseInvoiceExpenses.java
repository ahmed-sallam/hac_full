package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "PurchaseInvoiceExpenses")
@Table(name = "purchase_invoice_expenses")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PurchaseInvoiceExpenses extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private PurchaseExpensesTitle title;
    private Double amount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_invoice_id", nullable = false)
    private PurchaseInvoice purchaseInvoice;
}
