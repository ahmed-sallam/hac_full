import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppService} from "../../../app.service";
import {BidSummaryResponse} from "./interfaces/BidSummaryResponse";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class BidSummaryService {

    constructor(private http: HttpClient, private appService: AppService) {
    }

    addBidSummary(r: any): any {
        const link = `${this.appService.baseApi}/bid_summaries`;
        return this.http.post<any>(link, r);
    }

    getBidSummaries(page: number, sort?: string, size?: number,
                    search?: string, ref?: number,
                    user?: number, phase?: string, status?: string): Observable<BidSummaryResponse>{
        let link = `${this.appService.baseApi}/bid_summaries?page=${page}`;
        if (sort) {
            link += `&sort=${sort}`;
        }
        if (ref) {
            link += `&ref=${ref}`;
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
        return this.http.get<BidSummaryResponse>(link);
    }

}
