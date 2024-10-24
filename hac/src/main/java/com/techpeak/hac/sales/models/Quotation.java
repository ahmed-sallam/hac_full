package com.techpeak.hac.sales.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.core.models.CurrencyEntity;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.purchase.enums.PaymentTerms;
import com.techpeak.hac.purchase.enums.RequestStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "Quotation")
@Table(name = "quotations", indexes = {
        @Index(name = "idx_sales_quotation_date", columnList = "date"),
        @Index(name = "idx_quotation_customer_id", columnList = "customer_id"),
        @Index(name = "idx_quotation_currency_id", columnList = "currency_id"),
        @Index(name = "idx_sale_quotation_number", columnList = "number")
})
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quotation extends BaseEntity {

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.DRAFT;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @Column(name = "sub_total", nullable = false)
    private BigDecimal subTotal;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "vat")
    private BigDecimal vat;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Column(name = "notes")
    private String notes;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "valid_to")
    private LocalDate validUntil;

    @Column(name = "payment_terms")
    @Enumerated(EnumType.STRING)
    private PaymentTerms paymentTerms = PaymentTerms.IMMEDIATELY;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "internal_ref_id", nullable = false)
    @ToString.Exclude
    private InternalRef internalRef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    @ToString.Exclude
    private CurrencyEntity currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quotation", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<QuotationLine> lines = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    // @OneToMany(fetch = FetchType.LAZY)
    // @ToString.Exclude
    // private Set<UserHistory> userHistories = new HashSet<>();

    public void setLines(Set<QuotationLine> lines) {
        this.lines = new HashSet<>();
        lines.forEach(l -> l.setQuotation(this));
        this.lines.addAll(lines);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Quotation that))
            return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
