package com.techpeak.hac.inventory.models;

import com.techpeak.hac.core.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name="Brand")
@Table( name="brands", indexes = {
        @Index(name = "idx_is_active", columnList = "is_active"),
})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Brand extends BaseEntity{
  @org.hibernate.annotations.Index(name = "idx_name_ar", columnNames = "LOWER(name_ar)")
  @Column(name = "name_ar", length = 20, nullable = false)
  private String nameAr;
  @org.hibernate.annotations.Index(name = "idx_name_en", columnNames = "LOWER(name_en)")
  @Column(name = "name_en", length = 20, nullable = false)
  private String nameEn;
  @org.hibernate.annotations.Index(name = "idx_code", columnNames = "LOWER(code)")
  @Column(name = "code", length = 10)
  private String code;
  @Column(name="main_brand_id")
  @org.hibernate.annotations.Index(name = "idx_brand", columnNames = "main_brand_id")
  private Long mainBrand;
  @OneToMany(mappedBy = "mainBrand", fetch = FetchType.EAGER)
  private Set<Brand> subBrands = new HashSet<>();
}
