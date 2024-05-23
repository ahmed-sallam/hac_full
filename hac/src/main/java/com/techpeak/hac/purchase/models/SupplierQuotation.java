package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity(name = "SupplierQuotation")
@Table(name = "supplier_quotations")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SupplierQuotation extends BaseEntity {
    @Column(name = "notes")
    private String notes;
    private LocalDate date;
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

    @Column(name = "supplier_ref")
    private String supplierRef;
    @OneToOne
    @JoinColumn(name="internal_ref_id", nullable = false)
    private InternalRef internalRef;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    @OneToMany(mappedBy = "supplierQuotation")
    @ToString.Exclude
    private Set<SupplierQuotationExpenses> expenses;
    @OneToMany(mappedBy = "supplierQuotation")
    @ToString.Exclude
    private Set<SupplierQuotationLine> lines;

}
