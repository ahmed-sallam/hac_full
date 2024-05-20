package com.techpeak.hac.core.models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "Privilege")
@Table(name = "provileges")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Privilege extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles;

}
