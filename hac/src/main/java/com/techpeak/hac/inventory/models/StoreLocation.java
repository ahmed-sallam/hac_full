package com.techpeak.hac.inventory.models;

import com.techpeak.hac.core.models.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name="StoreLocation")
@Table( name="store_locations")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class StoreLocation extends BaseEntity {
  @Column(name = "name_ar", length = 20, nullable = false)
  private String nameAr;
  @Column(name = "name_en", length = 20, nullable = false)
  private String nameEn;
  @ManyToOne
  @JoinColumn(name="store_id")
  private Store store;
}
