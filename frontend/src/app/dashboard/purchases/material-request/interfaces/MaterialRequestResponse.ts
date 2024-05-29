import {Pageable, Sort2} from "../../../inventory/brands/BrandsResponse";

export interface MaterialRequestResponse {
    content: MaterialRequestShort[]
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

export interface MaterialRequestShort {
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

// export interface Pageable {
//     pageNumber: number
//     pageSize: number
//     sort: Sort
//     offset: number
//     paged: boolean
//     unpaged: boolean
// }
//
// export interface Sort {
//     empty: boolean
//     unsorted: boolean
//     sorted: boolean
// }
//
// export interface Sort2 {
//     empty: boolean
//     unsorted: boolean
//     sorted: boolean
// }
