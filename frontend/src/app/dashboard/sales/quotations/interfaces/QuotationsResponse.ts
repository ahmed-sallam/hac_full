import { Pageable, Sort } from "../../../inventory/brands/BrandsResponse";

export interface QuotationsResponse {
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  size: number;
  content: QuotationShort[];
  number: number;
  sort: Sort;
  numberOfElements: number;
  pageable: Pageable;
  empty: boolean;
}

export interface QuotationShort {
  id: number;
  isActive: boolean;
  status: string;
  number: string;
  total: number;
  date: string;
  paymentTerms: string;
  internalRef: InternalRef;
  user: User;
  rfpq: Rfpq;
  customer: Customer;
}

export interface InternalRef {
  id: number;
  currentPhase: string;
}

export interface User {
  id: number;
  username: string;
}

export interface Rfpq {
  id: number;
  number: string;
}

export interface Customer {
  id: number;
  nameAr: string;
  nameEn: string;
}
