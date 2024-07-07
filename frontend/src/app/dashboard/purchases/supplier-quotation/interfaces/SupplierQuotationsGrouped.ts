export interface SupplierQuotationsGrouped {
    products: Product[]
    suppliers: Supplier[]
    quotations: QuotationS[]
}

export interface Product {
    id: number
    productNumber: string
    subBrandAr: string
    subBrandEn: string
    quantity: number
    soldQuantity: number
}

export interface Supplier {
    id: number
    nameAr: string
    nameEn: string
}

export interface QuotationS {
    [key: string] :  Quotation[]
}

export interface Quotation {
    id: number
    date: string
    currency: Currency
    isLocal: boolean
    paymentTerms: string
    internalRef: any
    rfpqId: number
    netPrice: number
    sarPrice: number
    product: OneProduct
    delivery: string
}

export interface Currency {
    id: number
    name: string
    code: string
    exchangeRate: number
}

export interface OneProduct {
    id: number
    productNumber: string
    subBrandAr: string
    subBrandEn: string
}
