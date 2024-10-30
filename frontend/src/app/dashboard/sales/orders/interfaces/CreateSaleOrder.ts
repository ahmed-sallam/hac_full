export interface CreateSaleOrder {
    notes: string
    store: number
    date: string
    discount: number
    customer: number
    currency: number
    lines: OrderLine[]
    paymentTerms: string
    status: string
    deliveryDate: string
}

export interface OrderLine {
    quantity: number
    price: number
    discount: number
    notes: string
    productId: number
}
