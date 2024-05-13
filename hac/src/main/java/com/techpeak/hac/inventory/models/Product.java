package com.techpeak.hac.inventory.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.core.models.Country;
import com.techpeak.hac.inventory.enums.ProductUnit;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name="Product")
@Table( name="products", uniqueConstraints = {@UniqueConstraint(columnNames = {"product_number", "main_brand_id", "sub_brand_id", "country_id"})},
indexes={@Index(name = "idx_product_number", columnList = "product_number")}
)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product extends BaseEntity {
  @Column(name="product_number", nullable = false, length = 25)
  private String productNumber;
  @Column(name="description_ar", nullable = false, length = 255)
  private String descriptionAr;
  @Column(name="description_en", nullable = false, length = 255)
  private String descriptionEn;
  @Column(name="image", length = 255)
  private String image;
  @Column(name="part_image", length = 255)
  private String partImage;
  @Column(name="min_quantity")
  private Integer minQuantity;
  @Column(name="is_original",nullable = false )
  private Boolean isOriginal;
  @Column(name="sell_quantity",nullable = false )
  private Integer sellQuantity = 0;
  @Column(name="sell_individual",nullable = false )
  private Integer sellIndividual = 0;
  @Column(name="buy_quantity",nullable = false )
  private Integer buyQuantity = 0;
  @Column(name="buy_individual",nullable = false )
  private Integer buyIndividual = 0;
//  @Column(name="sell_as",nullable = false )
//  @Enumerated(EnumType.STRING)
//  private SellType sellAs = SellType.PIECE;
  @Column(name="unit",nullable = false )
  @Enumerated(EnumType.STRING)
  private ProductUnit unit = ProductUnit.PIECE;

  @ManyToOne
  @JoinColumn(name="machine_part_id", nullable = false)
  private MachinePart machinePart;
  @ManyToOne
  @JoinColumn(name="main_brand_id", nullable = false)
  private Brand mainBrand;
  @ManyToOne
  @JoinColumn(name="sub_brand_id", nullable = false)
  private Brand subBrand;
  @ManyToOne
  @JoinColumn(name="country_id", nullable = false)
  private Country country;
  @OneToMany(mappedBy = "productSet", fetch =FetchType.LAZY)
  @ToString.Exclude
  @JsonManagedReference
  private Set<ProductSet> setItems = new HashSet<>();

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "machinery_type_id", nullable = false)
  private MachineryType machineryType;

  @ManyToOne
  @JoinColumn(name = "machinery_model_id", nullable = false)
  private MachineryModel machineryModel;
}



