package com.techpeak.hac.inventory.models;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Alternative")
@Table(name = "alternatives", uniqueConstraints = {@UniqueConstraint(name = "alternative_products_unique", columnNames = {"product_1_number", "product_2_number"})})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Alternative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_1_number", nullable = false, length = 25)
    private String product1Number;
    @Column(name = "product_2_number", nullable = false, length = 25)
    private String product2Number;

}
