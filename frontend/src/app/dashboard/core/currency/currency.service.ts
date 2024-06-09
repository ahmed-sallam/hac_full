import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppService} from "../../../app.service";
import {Observable} from "rxjs";
import {Currency} from "./interfaces/Currency";

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {

  constructor(private http: HttpClient, private appService: AppService) {}

  getCurrencies() : Observable<Currency[]> {
    return this.http.get<Currency[]>(`${this.appService.baseApi}/currencies`);
  }
}
