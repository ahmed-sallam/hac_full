package com.techpeak.hac.purchase.dtos.bid_summary;

import com.techpeak.hac.inventory.dtos.ProductResponseXShort;
import com.techpeak.hac.purchase.dtos.CurrencyEntityDto;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QuotationOnBidSummary {
    private Long id;
    private String date;
    private CurrencyEntityDto currency;
    private Boolean isLocal;
    private String paymentTerms;
    private Long internalRef;
    private Long rfpqId;
    private Double netPrice;
    private Double sarPrice;
    private ProductResponseXShort product;
    private String delivery;
}
