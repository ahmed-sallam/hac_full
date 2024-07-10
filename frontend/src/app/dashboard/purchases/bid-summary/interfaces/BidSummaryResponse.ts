import {Pageable, Sort} from "../../../inventory/brands/BrandsResponse";
import {User} from "../../rfpq/interfaces/RFPQResponse";

export interface BidSummaryResponse{
    totalElements: number
    totalPages: number
    first: boolean
    last: boolean
    size: number
    content: BidSummaryShort[]
    number: number
    sort: Sort
    numberOfElements: number
    pageable: Pageable
    empty: boolean
}

export interface BidSummaryShort {
    id: number
    createdAt: string
    number: string
    status: string
    internalRef: number
    currentPhase: string
    rfpqId: number
    rfpqNumber: string
    user: User
}




