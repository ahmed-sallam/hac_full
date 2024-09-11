import { Pageable, Sort } from "../../../inventory/brands/BrandsResponse"

export interface CustomersResponse {

  totalPages: number;
  totalElements: number;
  size: number;
  content: CustomerEntity[];
  number: number;
  sort: Sort;
  pageable: Pageable;
  numberOfElements: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

export interface CustomerEntity {
  id: number;
  createdAt: string;
  updatedAt: string;
  isActive: boolean;
  nameAr: string;
  nameEn: string;
  email: string;
  phone: string;
  address: string;
}
