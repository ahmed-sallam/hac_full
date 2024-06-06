import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppService} from "../../app.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PurchaseService {

  constructor(private http: HttpClient, private appService: AppService) {}
  public getExpensesTitles(): Observable<PurchaseExpensesTitle[]> {
    let link = `${this.appService.baseApi}/purchase-expenses/titles`;

    return this.http.get<PurchaseExpensesTitle[]>(link);
  }
}
export interface PurchaseExpensesTitle{
  id: number;
  nameAr: string;
  nameEn: string;
  isActive: string;
  createdAt: string;
  updatedAt: string;
}
