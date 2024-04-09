import {Pageable, ProductEntity} from "../../products/interfaces/ListProductsResponse";
import {Sort2} from "../../machine-parts/interfaces/MachinePartResponse";
import {LocationEntity, StoreEntity} from "../../stores/interfaces/StoreResponse";

export interface StockResponse {
    content: StockEntity[]
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

export interface StockEntity {
    id: number
    createdAt: string
    updatedAt: any
    isActive: any
    qunatity: number
    product: ProductEntity
    store: StoreEntity
    location?: LocationEntity
}