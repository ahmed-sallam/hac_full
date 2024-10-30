import {AppService} from "../../../app.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {SaleOrdersResponse} from "./interfaces/SaleOrdersResponse";
import {CreateSaleOrder} from "./interfaces/CreateSaleOrder";
import {OneSaleOrder} from "./interfaces/OneSaleOrder";

@Injectable({
    providedIn: "root",
})
export class OrdersService {

    constructor(private http: HttpClient, private appService: AppService) {
    }

    getOrders(
        page: number,
        sort?: string,
        size?: number,
        search?: string,
        ref?: number,
        user?: number,
        status?: string,
        customer?: number,
        date?: string,
    ): Observable<SaleOrdersResponse> {
        let link: string = `${this.appService.baseApi}/sale-orders?page=${page}`;
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
            link += `&order=${search}`;
        }
        if (date) {
            link += `&date=${date}`;
        }
        if (customer) {
            link += `&customer=${customer}`;
        }
        return this.http.get<SaleOrdersResponse>(link);
    }

    createSaleOrder(saleOrder: CreateSaleOrder): Observable<any> {
        let link: string = `${this.appService.baseApi}/sale-orders`;
        return this.http.post<any>(link, saleOrder);
    }

    getOneSaleOrder(id: number): Observable<OneSaleOrder> {
        let link: string = `${this.appService.baseApi}/sale-orders/${id}`;
        return this.http.get<OneSaleOrder>(link);
    }

    updateSaleOrder(id: number, status: string): Observable<any> {
        const headers = new HttpHeaders({
            "Content-Type": "application/json",
        });
        let link: string = `${this.appService.baseApi}/sale-orders/${id}`;
        return this.http.patch<any>(link, JSON.stringify(status), {
            headers,
            withCredentials: true,
        });
    }
}
