import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppService} from "../../../app.service";

@Injectable({
  providedIn: 'root'
})
export class BidSummaryService {

  constructor(private http: HttpClient, private appService: AppService) {}
  public addBidSummary(r: any): any {
    const link = `${this.appService.baseApi}/bid_summaries`;
    return this.http.post<any>(link, r);
  }

}
