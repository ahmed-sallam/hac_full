package com.techpeak.hac.purchase.models;

import com.techpeak.hac.core.models.BaseEntity;
import com.techpeak.hac.core.models.InternalRef;
import com.techpeak.hac.core.models.User;
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
    @Column(name="status",nullable = false )
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.DRAFT;
    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;
    @ManyToOne
    @JoinColumn(name="internal_ref_id", nullable = false)
    private InternalRef internalRef;
    @ManyToOne
    @JoinColumn(name="rfpq_id", nullable = false)
    private RFPQ rfpq;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "bidSummary")
    private Set<BidSummaryLine> lines;

    public void setLines(Set<BidSummaryLine> lines) {
        this.lines = lines;
        for (BidSummaryLine line : lines) {
            line.setBidSummary(this);
        }
    }
}
