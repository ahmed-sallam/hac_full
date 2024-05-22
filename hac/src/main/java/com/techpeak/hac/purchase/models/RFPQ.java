package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.inventory.models.Store;
import com.techpeak.hac.purchase.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "RFPQ")
@Table(name = "rfpqs", 
        indexes = {
        @Index(name = "idx_rfpqs_number", columnList = "number"),
        @Index(name = "idx_rfpqs_status", columnList = "status"),
})
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RFPQ extends BaseEntity {
    private String number;
    private String notes;
    private LocalDate date;
    @Column(name="status",nullable = false )
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.DRAFT;
    @ManyToOne
    @JoinColumn(name="store_id" , nullable = false)
    private Store store; //
    @OneToOne
    @JoinColumn(name="internal_ref_id", nullable = false)
    private InternalRef internalRef;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
}
