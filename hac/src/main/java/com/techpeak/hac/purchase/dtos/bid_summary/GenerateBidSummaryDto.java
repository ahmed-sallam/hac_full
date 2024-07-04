package com.techpeak.hac.purchase.dtos.bid_summary;

import com.techpeak.hac.inventory.dtos.ProductResponseXShort;
import com.techpeak.hac.purchase.dtos.SupplierResponseName;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GenerateBidSummaryDto implements Serializable {
    private List<ProductResponseXShort> products;
    private List<SupplierResponseName> suppliers;
    private List<Map<String, QuotationOnBidSummary>> quotations;
}
/*
* id, date, currency,  isLocal, paymentTerms,  internalRef, rfpq_id, net_price, SAR price, product, delivery
*
* */