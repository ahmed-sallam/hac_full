package com.techpeak.hac.core.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity(name = "CurrencyEntity")
@Table(name = "currencies")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CurrencyEntity extends BaseEntity{
    @Column(name = "name")
    private String name;
    @Column(name = "code")
    private String code;
    @Column(name = "exchange_rate")
    private float exchangeRate;
}
