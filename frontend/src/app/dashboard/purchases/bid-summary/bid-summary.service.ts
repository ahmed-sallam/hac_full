import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AppService} from "../../../app.service";
import {BidSummaryResponse} from "./interfaces/BidSummaryResponse";
import {Observable} from "rxjs";
import {OneBidSummaryResponse} from "./interfaces/OnBidSummaryResponse";

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

    getOneBidSummary(id: number): Observable<OneBidSummaryResponse> {
        const link = `${this.appService.baseApi}/bid_summaries/${id}`;
        return this.http.get<OneBidSummaryResponse>(link);
    }

    getBidSummaries(page: number, sort?: string, size?: number,
                    search?: string, ref?: number,
                    user?: number, phase?: string, status?: string): Observable<BidSummaryResponse> {
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

    updateBidSummaryStatus(bidSummaryId: number, status: string): Observable<any> {
        const headers = new HttpHeaders({
            'Content-Type': 'application/json',
        });
        const link = `${this.appService.baseApi}/bid_summaries/${bidSummaryId}`;
        return this.http.patch<any>(link, JSON.stringify(status), {
            headers,
            withCredentials: true
        });
    }
}
