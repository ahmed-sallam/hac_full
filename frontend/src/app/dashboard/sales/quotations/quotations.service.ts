import { HttpClient, HttpHeaders } from "@angular/common/http";

import { AppService } from "../../../app.service";
import { CreateQuotation } from "./interfaces/CreateQuotation";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { OneQuotation } from "./interfaces/OneQuotation";
import { QuotationsResponse } from "./interfaces/QuotationsResponse";

@Injectable({
  providedIn: "root",
})
export class QuotationsService {
  constructor(private http: HttpClient, private appService: AppService) {}

  getQuotations(
    page: number,
    sort?: string,
    size?: number,
    search?: string,
    ref?: number,
    user?: number,
    status?: string,
    date?: string
  ): Observable<QuotationsResponse> {
    let link: string = `${this.appService.baseApi}/quotations?page=${page}`;
    if (sort) {
      link += `&sort=${sort}`;
    }
    if (ref) {
      link += `&ref=${ref}`;
    }

    if (user) {
      link += `&user=${user}`;
    }

    if (status) {
      link += `&status=${status}`;
    }
    if (size) {
      link += `&size=${size}`;
    }
    if (search) {
      link += `&quotation=${search}`;
    }
    if (date) {
      link += `&date=${date}`;
    }
    return this.http.get<QuotationsResponse>(link);
  }

  getOneQuotation(id: number): Observable<OneQuotation> {
    let link: string = `${this.appService.baseApi}/quotations/${id}`;
    return this.http.get<OneQuotation>(link);
  }

  createQuotation(quotation: CreateQuotation): Observable<any> {
    let link: string = `${this.appService.baseApi}/quotations`;
    return this.http.post<any>(link, quotation);
  }

  // updateQuotation(id: number, quotation: CreateQuotation): Observable<any> {
  //   let link: string = `${this.appService.baseApi}/quotations/${id}`;
  //   return this.http.patch<any>(link, quotation);
  // }

  // deleteQuotation(id: number): Observable<any> {
  //   let link: string = `${this.appService.baseApi}/quotations/${id}`;
  //   return this.http.delete<any>(link);
  // }

  updateQuotationStatus(id: number, status: string): Observable<any> {
    const headers = new HttpHeaders({
      "Content-Type": "application/json",
    });
    let link: string = `${this.appService.baseApi}/quotations/${id}`;
    return this.http.patch<any>(link, JSON.stringify(status), {
      headers,
      withCredentials: true,
    });
  }
}
