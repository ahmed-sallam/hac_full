export interface CreateSupplierQuotation {
    notes: string
    date: string
    validTo: string
    receiveIn: string
    currencyId: number
    subTotal: number
    discount: number
    vat: number
    totalExpenses: number
    total: number
    isLocal: boolean
    paymentTerms: string
    supplierRef: string
    internalRefId: number
    userId: number
    rfpqId: number
    supplierId: number
    expenses: SupplierQuotationExpense[]
    lines: SupplierQuotationLine[]
}

export interface SupplierQuotationExpense {
    expensesTitleId: number
    amount: number
    notes: string
}

export interface SupplierQuotationLine {
    quantity: number
    price: number
    discount: number
    total: number
    notes: string
    productId: number
}
