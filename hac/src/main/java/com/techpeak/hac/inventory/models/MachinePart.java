package com.techpeak.hac.inventory.models;

import com.techpeak.hac.core.models.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "MachinePart")
@Table(name = "machine_parts", indexes = {
        @Index(name = "idx_machine_parts_is_active", columnList = "is_active"),
})
// create index on nameAr or nameEn
// @Table( name="machine_parts" ,indexes = {@Index(name =
// "idx_machine_parts_is_active", columnList = "is_active"), @Index(name =
// "idx_machine_parts_name_ar", columnList = "name_ar"), @Index(name =
// "idx_machine_parts_name_en", columnList = "name_en")})

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MachinePart extends BaseEntity {
    // @org.hibernate.annotations.Index(name = "idx_machine_parts_name_ar",
    // columnNames = "LOWER(name_ar)")
    @Column(name = "name_ar", length = 20, nullable = false)
    private String nameAr;
    // @org.hibernate.annotations.Index(name = "idx_machine_parts_name_en",
    // columnNames = "LOWER(name_en)")
    @Column(name = "name_en", length = 20, nullable = false)
    private String nameEn;

}
