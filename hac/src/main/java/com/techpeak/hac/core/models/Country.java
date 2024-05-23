package com.techpeak.hac.core.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity(name="Country")
@Table( name="countries")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Country implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "name_ar", length = 30, nullable = false)
  private String nameAr;
  @Column(name = "name_en", length = 30, nullable = false)
  private String nameEn;

}
