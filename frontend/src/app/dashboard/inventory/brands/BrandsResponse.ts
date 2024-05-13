export interface BrandsResponse {
    content: BrandEntity[]
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

export interface BrandEntity {
    id: number
    nameAr: string
    nameEn: string
    code: any
    subBrands: SubBrand[]|any
    isActive: boolean
    createdAt: string
    updatedAt?: string
}

export interface SubBrand {
    id: number
    createdAt: string
    updatedAt?: string
    isActive: boolean
    nameAr: string
    nameEn: string
    code: any
    mainBrand: number
    subBrands: any[]
}

export interface Pageable {
    pageNumber: number
    pageSize: number
    sort: Sort
    offset: number
    paged: boolean
    unpaged: boolean
}

export interface Sort {
    empty: boolean
    unsorted: boolean
    sorted: boolean
}

export interface Sort2 {
    empty: boolean
    unsorted: boolean
    sorted: boolean
}
