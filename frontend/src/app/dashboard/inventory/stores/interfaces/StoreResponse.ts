export interface StoreResponse {
  content: StoreEntity[];
  pageable: Pageable;
  last: boolean;
  totalElements: number;
  totalPages: number;
  first: boolean;
  size: number;
  number: number;
  sort: Sort2;
  numberOfElements: number;
  empty: boolean;
}

export interface StoreEntity {
  id: number;
  nameAr: string;
  nameEn: string;
  cityAr: string;
  cityEn: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: any;
  locations: LocationEntity[];
}

export interface LocationEntity {
  id: number;
  nameAr: string;
  nameEn: string;
  storeId: number;
  isActive: boolean;
  createdAt: string;
  updatedAt: any;
}

export interface Pageable {
  pageNumber: number;
  pageSize: number;
  sort: Sort;
  offset: number;
  paged: boolean;
  unpaged: boolean;
}

export interface Sort {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

export interface Sort2 {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

export interface CreateStore {
  nameAr: string;
  nameEn: string;
  cityAr: string;
  cityEn: string;
  isActive?: boolean;
}
