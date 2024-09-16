export interface OneQuotation {
  id: number;
  createdAt: string;
  updatedAt: string;
  isActive: boolean;
  status: string;
  number: string;
  subTotal: number;
  discount: number;
  vat: number;
  total: number;
  notes: string;
  date: string;
  validUntil: string;
  paymentTerms: string;
  internalRef: {
    id: number;
    currentPhase: string;
  };
  user: {
    id: number;
    username: string;
  };
  rfpq: {
    id: number;
    number: string;
  };
  customer: {
    id: number;
    createdAt: string;
    updatedAt: string;
    isActive: boolean;
    nameAr: string;
    nameEn: string;
    email: string;
    phone: string;
    address: string;
  };
  lines: Line[];
  userHistories: {
    id: number;
    actionDetails: string;
    tableName: string;
    recordId: number;
    dateTime: string;
    user: {
      id: number;
      username: string;
    };
  };
}

interface Line {
  id: number;
  createdAt: string;
  updatedAt: string;
  isActive: boolean;
  quantity: number;
  price: number;
  discount: number;
  total: number;
  notes: string;
  product: {
    id: number;
    productNumber: string;
    descriptionAr: string;
    descriptionEn: string;
    mainBrandAr: string;
    mainBrandEn: string;
    subBrandAr: string;
    subBrandEn: string;
    totalInventory: number;
  };
}
