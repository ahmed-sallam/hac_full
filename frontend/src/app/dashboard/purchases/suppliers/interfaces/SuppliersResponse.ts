import {Pageable, Sort2} from "../../../inventory/brands/BrandsResponse";

export interface SuppliersResponse{
    content: SupplierEntity[]
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
export interface SupplierEntity {
    id:number;
    nameAr: string;
    nameEn: string;
    email: string;
    phone: string;
    address: string;
    isActive: boolean;
    createdAt?: string;
    updatedAt?: string;

}