import {UserDto} from "../../material-request/interfaces/OneMaterialRequest";
import {
    SupplierQuotationsGrouped
} from "../../supplier-quotation/interfaces/SupplierQuotationsGrouped";

export interface OneBidSummaryResponse {
    id: number
    number: string
    status: string
    fromDate: string
    updatedAt: string
    internalRef: number
    currentPhase: string
    rfpqId: number
    rfpqNumber: string
    user: UserDto
    lines: OneBidSummaryLine[]
    generateBidSummary: SupplierQuotationsGrouped
}


export interface OneBidSummaryLine {
    id: number
    price: number
    vat: number
    total: number
    quantity: number
    quotationId: number
    productId: number
    supplierId: number
}
