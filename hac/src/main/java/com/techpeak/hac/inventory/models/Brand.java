package com.techpeak.hac.inventory.models;

import com.techpeak.hac.core.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name="Brand")
@Table( name="brands", indexes = {
        @Index(name = "idx_is_active", columnList = "is_active"),
        @Index(name = "idx_brands_name_ar", columnList = "LOWER(name_ar)"),
        @Index(name = "idx_brands_name_en", columnList = "LOWER(name_en)"),
        @Index(name = "idx_brands_code", columnList = "LOWER(code)"),
        @Index(name = "idx_brands_brand", columnList = "main_brand_id"),
})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Brand extends BaseEntity{
  @Column(name = "name_ar", length = 20, nullable = false)
  private String nameAr;
  @Column(name = "name_en", length = 20, nullable = false)
  private String nameEn;
  @Column(name = "code", length = 10)
  private String code;
  @Column(name="main_brand_id")
  private Long mainBrand;
  @OneToMany(mappedBy = "mainBrand", fetch = FetchType.EAGER)
  private Set<Brand> subBrands = new HashSet<>();
}

