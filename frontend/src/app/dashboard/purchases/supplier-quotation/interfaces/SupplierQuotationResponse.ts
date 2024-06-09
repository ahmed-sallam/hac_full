import {Pageable, Sort2} from "../../../inventory/brands/BrandsResponse";

export interface SupplierQuotationResponse {
    content: SupplierQuotationShort[]
    pageable: Pageable
    last: boolean
    totalElements: number
    totalPages: number
    first: boolean
    size: number
    number: number
    sort: Sort2
    numberOfElements: number
    empty: boolean
}

export interface SupplierQuotationShort {
    id: number
    isActive: boolean
    date: string
    currency: Currency
    total: number
    isLocal: boolean
    paymentTerms: string
    supplierRef: string
    internalRef: InternalRef
    user: User
    rfpq: Rfpq
    supplier: Supplier
}

export interface Currency {
    id: number
    code: string
}

export interface InternalRef {
    id: number
    currentPhase: string
}

export interface User {
    id: number
    username: string
}

export interface Rfpq {
    id: number
    number: string
}

export interface Supplier {
    id: number
    nameAr: string
    nameEn: string
}



