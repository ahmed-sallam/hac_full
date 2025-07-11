package com.techpeak.hac.inventory.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.techpeak.hac.core.models.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "MachineryModel")
@Table(name = "machinery_model")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MachineryModel extends BaseEntity {
    // @org.hibernate.annotations.Index(name = "idx_machinery_model_name_ar",
    // columnNames = "LOWER(name_ar)")
    @Column(name = "name_ar", length = 25, nullable = false)
    private String nameAr;
    // @org.hibernate.annotations.Index(name = "idx_machinery_model_name_en",
    // columnNames = "LOWER(name_en)")
    @Column(name = "name_en", length = 25, nullable = false)
    private String nameEn;
    @ManyToOne
    @JoinColumn(name = "machinery_type_id")
    @JsonBackReference
    private MachineryType machineryType;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
}
