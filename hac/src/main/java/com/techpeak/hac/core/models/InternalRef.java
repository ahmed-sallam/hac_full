package com.techpeak.hac.core.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity(name="InternalRef")
@Table( name="internal_refs")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InternalRef implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
