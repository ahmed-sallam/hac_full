export interface OneSaleOrder {
    id: number
    createdAt: string
    updatedAt: string
    isActive: boolean
    status: string
    number: string
    subTotal: number
    discount: number
    vat: number
    total: number
    notes: string
    date: string
    paymentTerms: string
    internalRef: InternalRef
    user: User
    customer: Customer
    lines: OrderLine[]
    userHistories: UserHistory[]
    deliveryDate: string
    store: Store
}

export interface InternalRef {
    id: number
    currentPhase: string
}

export interface User {
    id: number
    username: string
}

export interface Customer {
    id: number
    createdAt: string
    updatedAt: string
    isActive: boolean
    nameAr: string
    nameEn: string
    email: string
    phone: string
    address: string
}

export interface OrderLine {
    id: number
    createdAt: string
    updatedAt: string
    isActive: boolean
    quantity: number
    price: number
    discount: number
    total: number
    notes: string
    product: Product
}

export interface Product {
    id: number
    productNumber: string
    descriptionAr: string
    descriptionEn: string
    mainBrandAr: string
    mainBrandEn: string
    subBrandAr: string
    subBrandEn: string
    totalInventory: number
}

export interface UserHistory {
    id: number
    actionDetails: string
    tableName: string
    recordId: number
    dateTime: string
    user: User2
}

export interface User2 {
    id: number
    username: string
}

export interface Store {
    id: number
    nameAr: string
    nameEn: string
}
