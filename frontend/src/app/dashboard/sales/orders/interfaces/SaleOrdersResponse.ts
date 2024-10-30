export interface SaleOrdersResponse {
    totalPages: number
    totalElements: number
    first: boolean
    last: boolean
    size: number
    content: SaleOrderContent[]
    number: number
    sort: Sort
    pageable: Pageable
    numberOfElements: number
    empty: boolean
}

export interface SaleOrderContent {
    id: number
    isActive: boolean
    status: string
    number: string
    total: number
    date: string
    paymentTerms: string
    internalRef: InternalRef
    user: User
    customer: Customer
    discount: number
    deliveryDate: string
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
    nameAr: string
    nameEn: string
}

export interface Sort {
    empty: boolean
    sorted: boolean
    unsorted: boolean
}

export interface Pageable {
    offset: number
    sort: Sort2
    pageNumber: number
    pageSize: number
    paged: boolean
    unpaged: boolean
}

export interface Sort2 {
    empty: boolean
    sorted: boolean
    unsorted: boolean
}
