package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.inventory.models.Store;
import com.techpeak.hac.purchase.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "MaterialRequest")
@Table(name = "material_requests",
        indexes = {
        @Index(name = "idx_material_requests_number", columnList = "number"),
                @Index(name = "idx_material_requests_status", columnList = "status")
        })
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MaterialRequest extends BaseEntity {
    private String number;
    private String notes;
    private LocalDate date;
    @Column(name="status",nullable = false )
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.DRAFT;
    @ManyToOne
    @JoinColumn(name="store_id" , nullable = false)
    private Store store;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="internal_ref_id", nullable = false)
    private InternalRef internalRef;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "materialRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MaterialRequestLine> lines = new HashSet<>();

    public void addLine(MaterialRequestLine line){
        lines.add(line);
    }

    public void setLines(Set<MaterialRequestLine> lines) {
        this.lines = lines;
        for (MaterialRequestLine line : lines) {
            line.setMaterialRequest(this);
        }
    }

}
