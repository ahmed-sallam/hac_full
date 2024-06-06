import {Injectable} from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {AppService} from "../../../app.service";
import {Observable} from "rxjs";
import {StockResponse} from "./interfaces/StockResponse";
import {CreateStock} from "./interfaces/CreateStock";
import {StockResponseShort} from "../products/interfaces/StockResponseShort";

@Injectable({
  providedIn: 'root'
})
export class StockService {

  constructor(private http: HttpClient, private appService: AppService) {}


  public getStock(
      page: number,
      size?: number,
      productNumber?: string,
  ): Observable<StockResponse> {
    let link = `${this.appService.baseApi}/inventory?page=${page}`;
    if (size) {
      link += `&size=${size}`;
    }
    if (productNumber) {
      link += `&productNumber=${productNumber}`;
    }
    return this.http.get<StockResponse>(link);
  }

  public addStock(stock: CreateStock): Observable<void> {
    const link = `${this.appService.baseApi}/inventory`;
    return this.http.post<void>(link, stock);
  }

  getProductStockShort(productId: number):Observable<StockResponseShort[]>{
    const link = `${this.appService.baseApi}/inventory/products/${productId}`;
    return this.http.get<StockResponseShort[]>(link);

  }


}
