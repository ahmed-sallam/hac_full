package com.techpeak.hac.employees.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name="Position")
@Table( name="positions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Position {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "name_ar",nullable = false, length =100 )
  private String nameAr;
  @Column(name = "name_en",nullable = false, length =100)
  private String nameEn;
@OneToMany(mappedBy ="position")
  private Set<Employees> employees= new HashSet<>();
}
