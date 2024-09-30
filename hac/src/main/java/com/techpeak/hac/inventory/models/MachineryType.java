package com.techpeak.hac.inventory.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.techpeak.hac.core.models.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "MachineryType")
@Table(name = "machinery_type")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MachineryType extends BaseEntity {
    // @org.hibernate.annotations.Index(name = "idx_machinery_type_name_ar",
    // columnNames = "LOWER(name_ar)")
    @Column(name = "name_ar", length = 25, nullable = false)
    private String nameAr;
    // @org.hibernate.annotations.Index(name = "idx_machinery_type_name_en",
    // columnNames = "LOWER(name_en)")
    @Column(name = "name_en", length = 25, nullable = false)
    private String nameEn;

    @OneToMany(mappedBy = "machineryType", fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonManagedReference
    private Set<MachineryModel> machineryModels;
}
