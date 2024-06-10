import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppService} from "../../../app.service";
import {Observable} from "rxjs";
import {CreateSupplierQuotation} from "./interfaces/CreateSupplierQuotation";
import {
  SupplierQuotationResponse
} from "./interfaces/SupplierQuotationResponse";
import {SupplierQuotation} from "./interfaces/SupplierQuotation";

@Injectable({
  providedIn: 'root'
})
export class SupplierQuotationService {

  constructor(private http: HttpClient, private appService: AppService) {}
  public addSupplierQuotation(sq: CreateSupplierQuotation): Observable<number> {
    const link = `${this.appService.baseApi}/supplier-quotations`;
    return this.http.post<number>(link, sq);
  }


  getQuotations(page: number, sort?: string | undefined, pageSize?: number, searchName?: string, ref?: number | undefined, supplier?: number | undefined, user?: number | undefined, date?: Date | string | undefined, supplierRef?: string | undefined, isLocal?: boolean | undefined):Observable<SupplierQuotationResponse> {
    let link: string = `${this.appService.baseApi}/supplier-quotations?page=${page}`;
    if (sort) {
      link += `&sort=${sort}`;
    }
    if (pageSize) {
      link += `&size=${pageSize}`;
    }
    if (searchName) {
      link += `&rfpq=${searchName}`;
    }
    if (ref) {
      link += `&ref=${ref}`;
    }
    if (supplier) {
      link += `&supplier=${supplier}`;
    }
    if (user) {
      link += `&user=${user}`;
    }
    if (date) {
      link += `&date=${date}`;
    }
    if (supplierRef) {
      link += `&supplierRef=${supplierRef}`;
    }
    if (isLocal===true || isLocal===false) {
      link += `&isLocal=${isLocal}`;
    }
    return this.http.get<SupplierQuotationResponse>(link);
  }


  getOne(id: number): Observable<SupplierQuotation> {
    let link: string = `${this.appService.baseApi}/supplier-quotations/${id}`;
    return this.http.get<SupplierQuotation>(link);
  }
}
