import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppService} from "../../../app.service";
import {Observable} from "rxjs";
import {RFPQResponse} from "./interfaces/RFPQResponse";
import {OneRFPQ} from "./interfaces/OneRFPQ";

@Injectable({
  providedIn: 'root'
})
export class RfpqService {


  constructor(private http: HttpClient, private appService: AppService) {
  }


  getRFPQs(
      page: number, sort?: string, size?: number,
      search?: string, ref?: number, store?: number,
      user?: number, phase?: string, status?: string): Observable<RFPQResponse> {
    let link: string = `${this.appService.baseApi}/rfpqs?page=${page}`;
    if (sort) {
      link += `&sort=${sort}`;
    }
    if (ref) {
      link += `&ref=${ref}`;
    }

    if (store) {
      link += `&store=${store}`;
    }
    if (user) {
      link += `&user=${user}`;
    }
    if (phase) {
      link += `&phase=${phase}`;
    }
    if (status) {
      link += `&status=${status}`;
    }
    if (size) {
      link += `&size=${size}`;
    }
    if (search) {
      link += `&search=${search}`;
    }
    return this.http.get<RFPQResponse>(link);
  }

  getOneRFPQ(id: number): Observable<OneRFPQ> {
    let link: string = `${this.appService.baseApi}/rfpqs/${id}`;
    return this.http.get<OneRFPQ>(link);
  }


}
