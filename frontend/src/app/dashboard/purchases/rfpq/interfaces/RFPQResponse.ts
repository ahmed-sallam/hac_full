import {Pageable, Sort2} from "../../../inventory/brands/BrandsResponse";

export interface RFPQResponse {
    content: RFPQShort[]
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

export interface RFPQShort {
    id: number
    number: string
    date: string
    status: string
    store: Store
    internalRef: number
    user: User
    currentPhase: string
}

export interface Store {
    id: number
    nameAr: string
    nameEn: string
}

export interface User {
    id: number
    username: string
}

