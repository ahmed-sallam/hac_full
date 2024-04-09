package com.techpeak.hac.employees.models;

import com.techpeak.hac.core.models.Country;
import jakarta.persistence.*;
import lombok.*;

@Entity(name="Employees")
@Table( name="employees")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employees {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "name_ar",nullable = false, length =100 )
  private String nameAr;
  @Column(name = "name_en",nullable = false, length =100)
  private String nameEn;
  @Column(name = "national_id",nullable = false, length =20)
  private String nationalId;
@OneToOne
  @JoinColumn(name = "country_id",nullable = false)
  private Country  country;
@ManyToOne
  @JoinColumn(name = "position_id",nullable = false)
  private Position position;
}
