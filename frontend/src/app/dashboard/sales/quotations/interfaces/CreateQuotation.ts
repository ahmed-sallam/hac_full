export interface CreateQuotation {
  notes: string;
  validUntil: string;
  discount: number;
  customer: number;
  currency: number;
  lines: QuotationLine[];
  paymentTerms: string;
  status: string;
}

export interface QuotationLine {
  quantity: number;
  price: number;
  discount: number;
  notes: string;
  productId: number;
}
