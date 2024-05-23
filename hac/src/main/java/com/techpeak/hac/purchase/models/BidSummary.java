package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
import com.techpeak.hac.inventory.models.Store;
import com.techpeak.hac.purchase.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity(name = "BidSummary")
@Table(name = "bid_summary")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BidSummary extends BaseEntity {
    @Column(name = "number", nullable = false)
    private String number;
    @Column(name = "notes")
    private String notes;
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @Column(name="status",nullable = false )
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.DRAFT;
    @ManyToOne
    @JoinColumn(name="store_id" , nullable = false)
    private Store store;
    @OneToOne
    @JoinColumn(name="internal_ref_id", nullable = false)
    private InternalRef internalRef;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "bidSummary")
    private Set<BidSummaryLine> lines;
}
