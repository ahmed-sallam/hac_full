package com.techpeak.hac.inventory.models;

import com.techpeak.hac.core.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name="Store")
@Table( name="stores")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Store  extends BaseEntity {
  @org.hibernate.annotations.Index(name = "idx_name_ar", columnNames = "LOWER(name_ar)")
  @Column(name = "name_ar", length = 25, nullable = false)
  private String nameAr;
  @org.hibernate.annotations.Index(name = "idx_name_en", columnNames = "LOWER(name_en)")
  @Column(name = "name_en", length = 25, nullable = false)
  private String nameEn;
  @Column(name = "city_ar", length = 25, nullable = false)
  private String cityAr;
  @Column(name = "city_en", length = 25, nullable = false)
  private String cityEn;
  @OneToMany(mappedBy = "store", fetch = FetchType.EAGER)
  @ToString.Exclude
  private Set<StoreLocation> locations= new HashSet<>();
// todo add store type virtual or physical and related updates
}
