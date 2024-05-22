package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
@Entity(name = "PurchaseExpensesTitle")
@Table(name = "purchase_expenses_titles")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PurchaseExpensesTitle extends BaseEntity {
    @Column(name="name_ar", nullable = false)
    private String nameAr;
    @Column(name="name_en", nullable = false)
    private String nameEn;
}
