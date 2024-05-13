package com.techpeak.hac.inventory.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.techpeak.hac.core.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name="MachineryType")
@Table( name="machinery_type")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MachineryType extends BaseEntity {
    @org.hibernate.annotations.Index(name = "idx_name_ar", columnNames = "LOWER(name_ar)")
    @Column(name = "name_ar", length = 25, nullable = false)
    private String nameAr;
    @org.hibernate.annotations.Index(name = "idx_name_en", columnNames = "LOWER(name_en)")
    @Column(name = "name_en", length = 25, nullable = false)
    private String nameEn;

    @OneToMany(mappedBy = "machineryType", fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonManagedReference
    private Set<MachineryModel> machineryModels;
}
