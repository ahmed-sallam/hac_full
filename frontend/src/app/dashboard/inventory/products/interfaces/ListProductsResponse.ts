import {MachineryModelEntity, MachineryTypeEntity} from "../../machinery/interfaces/MachineryResponse";

export interface ListProductsResponse {
    content: ProductEntity[]
    pageable: Pageable
    last: boolean
    totalElements: number
    totalPages: number
    first: boolean
    size: number
    number: number
    sort: Sort
    numberOfElements: number
    empty: boolean
}

export interface ProductEntity {
    id: number
    productNumber: string
    descriptionAr: string
    descriptionEn: string
    image: string
    partImage: string
    minQuantity: number
    isOriginal: boolean
    unit: string
    machinePart: MachinePart
    mainBrand: BrandEntity
    subBrand: BrandEntity
    country: CountryEntity
    isActive: boolean
    createdAt: string
    updatedAt: any
    sameItems: ProductEntity[]
    alternatives: ProductEntity[]
    related: ProductEntity[]
    setItems: ProductEntity[]
    quantity?: number
    machineryModel: MachineryModelEntity
    machineryType: MachineryTypeEntity
    sellQuantity: number
    sellIndividual: number
    buyQuantity: number
    buyIndividual: number
    totalInventory: number

}

export interface MachinePart {
    id: number
    createdAt: string
    updatedAt: string
    isActive: boolean
    nameAr: string
    nameEn: string
}

export interface BrandEntity {
    id: number
    createdAt: string
    updatedAt: string|null
    isActive: boolean
    nameAr: string
    nameEn: string
    code: string|null
    mainBrand: any
    subBrands: BrandEntity[]
}

export interface CountryEntity {
    id: number
    nameAr: string
    nameEn: string
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
    sorted: boolean
    unsorted: boolean
}