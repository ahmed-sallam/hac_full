package com.techpeak.hac.sales.models;

import com.techpeak.hac.core.models.*;
import com.techpeak.hac.purchase.enums.PaymentTerms;
import com.techpeak.hac.purchase.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Quotation")
@Table(name = "quotations", indexes = {
        @Index(name = "idx_sales_quotation_date", columnList = "date"),
        @Index(name = "idx_quotation_customer_id", columnList = "customer_id"),
        @Index(name = "idx_quotation_currency_id", columnList = "currency_id"),
        @Index(name = "idx_sale_quotation_number", columnList = "number")
})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Quotation extends BaseEntity {
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.DRAFT;
    private String number;
    @Column(name = "sub_total")
    private Double subTotal;
    @Column(name = "discount")
    private Double discount;
    @Column(name = "vat")
    private Double vat;
    @Column(name = "total")
    private Double total;
    @Column(name = "notes")
    private String notes;
    private LocalDate date;
    @Column(name = "valid_to")
    private LocalDate validTo;
    @Column(name = "payment_terms")
    @Enumerated(EnumType.STRING)
    private PaymentTerms paymentTerms = PaymentTerms.IMMEDIATELY;
    @ManyToOne
    @JoinColumn(name = "internal_ref_id", nullable = false)
    private InternalRef internalRef;
    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private CurrencyEntity currency;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quotation")
    @ToString.Exclude
    private Set<QuotationLine> lines = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany
    @ToString.Exclude
    private Set<UserHistory> userHistories = new HashSet<>();

    public void setLines(Set<QuotationLine> lines) {
        this.lines.clear();
        this.lines.addAll(lines);
    }
}
