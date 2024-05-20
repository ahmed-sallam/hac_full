package com.techpeak.hac.core.models;

import jakarta.persistence.*;
import lombok.*;

@Entity(name="InternalRef")
@Table( name="internal_refs")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InternalRef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
