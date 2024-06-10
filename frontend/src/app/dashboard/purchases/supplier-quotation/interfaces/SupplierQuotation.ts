export interface SupplierQuotation {
    id: number
    createdAt: string
    updatedAt: any
    isActive: boolean
    notes: string
    date: string
    validTo: string
    receiveIn: string
    currency: Currency
    subTotal: number
    discount: number
    vat: number
    totalExpenses: number
    total: number
    isLocal: boolean
    paymentTerms: string
    supplierRef: string
    internalRef: InternalRef
    user: User
    rfpq: Rfpq
    supplier: Supplier
    expenses: any[]
    lines: any[]
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
