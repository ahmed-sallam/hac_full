package com.techpeak.hac.core.models;

import com.techpeak.hac.core.enums.InternalPhase;
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
    @Column(name="current_phase", nullable = false)
    @Enumerated(EnumType.STRING)
    private InternalPhase currentPhase;
}
