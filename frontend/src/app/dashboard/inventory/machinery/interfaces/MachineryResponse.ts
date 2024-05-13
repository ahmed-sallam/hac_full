import {BrandEntity, Pageable, Sort2} from "../../brands/BrandsResponse";

export interface MachineryResponse {
    content: MachineryTypeEntity[]
    pageable: Pageable
    last: boolean
    totalPages: number
    totalElements: number
    first: boolean
    size: number
    number: number
    sort: Sort2
    numberOfElements: number
    empty: boolean
}
export interface MachineryTypeEntity {
    id: number
    createdAt: string
    updatedAt: any
    isActive: boolean
    nameAr: string
    nameEn: string
    machineryModels: MachineryModelEntity[]
}
export interface MachineryModelEntity {
    id: number
    nameAr: string
    nameEn: string
    brand?: BrandEntity
    machineryType: number
    isActive: boolean
    createdAt: string
    updatedAt: any
}