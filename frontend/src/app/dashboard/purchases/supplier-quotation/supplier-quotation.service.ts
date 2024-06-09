import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppService} from "../../../app.service";
import {Observable} from "rxjs";
import {CreateSupplierQuotation} from "./interfaces/CreateSupplierQuotation";

@Injectable({
  providedIn: 'root'
})
export class SupplierQuotationService {

  constructor(private http: HttpClient, private appService: AppService) {}
  public addSupplierQuotation(sq: CreateSupplierQuotation): Observable<number> {
    const link = `${this.appService.baseApi}/supplier-quotations`;
    return this.http.post<number>(link, sq);
  }

}
