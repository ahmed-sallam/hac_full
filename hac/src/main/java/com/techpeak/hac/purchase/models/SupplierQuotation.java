package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.*;
import com.techpeak.hac.purchase.enums.PaymentTerms;
import com.techpeak.hac.purchase.enums.ReceiveTypes;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "SupplierQuotation")
@Table(name = "supplier_quotations", indexes = {
        @Index(name = "idx_date", columnList = "date"),
        @Index(name = "idx_supplier_id", columnList = "supplier_id"),
        @Index(name = "idx_currency_id", columnList = "currency_id")
})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SupplierQuotation extends BaseEntity {
    // use is active from BaseEntity
    @Column(name = "notes")
    private String notes;
    private LocalDate date;
    @Column(name = "valid_to")
    private LocalDate validTo;
    @Column(name = "receive_in")
    @Enumerated(EnumType.STRING)
    private ReceiveTypes receiveIn;
    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private CurrencyEntity currency;
    @Column(name = "sub_total")
    private Double subTotal;
    @Column(name = "discount")
    private Double discount;
    @Column(name = "vat")
    private Double vat;
    @Column(name = "total_expenses")
    private Double totalExpenses;
    @Column(name = "total")
    private Double total;
    @Column(name = "is_local")
    private Boolean isLocal = true;

    @Column(name = "payment_terms")
    @Enumerated(EnumType.STRING)
    private PaymentTerms paymentTerms = PaymentTerms.IMMEDIATELY;

    @Column(name = "supplier_ref")
    private String supplierRef;
    @ManyToOne
    @JoinColumn(name = "internal_ref_id", nullable = false)
    private InternalRef internalRef;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "rfpq_id", nullable = false)
    private RFPQ rfpq;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    @OneToMany(mappedBy = "supplierQuotation")
    @ToString.Exclude
    private Set<SupplierQuotationExpenses> expenses;
    @OneToMany(mappedBy = "supplierQuotation", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<SupplierQuotationLine> lines = new HashSet<>();
    @OneToMany
    @ToString.Exclude
    private Set<UserHistory> userHistories = new HashSet<>();

    public void setLines(Set<SupplierQuotationLine> lines) {
        this.lines = new HashSet<>();
        lines.forEach(l -> l.setSupplierQuotation(this));
        this.lines.addAll(lines);
    }

    public void setUserHistories(Set<UserHistory> userHistories) {
        this.userHistories = new HashSet<>();
        userHistories.forEach(uh -> uh.setRecordId(this.getId()));
        this.userHistories.addAll(userHistories);
    }

}
