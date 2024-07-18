package com.techpeak.hac.purchase.dtos.bid_summary;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OneBidSummaryLineDto implements Serializable {
    private Long id;
    private Double price;
    private Double vat;
    private Double total;
    private Integer quantity;
    private Long quotationId;
    private Long productId;
    private Long supplierId;

}
