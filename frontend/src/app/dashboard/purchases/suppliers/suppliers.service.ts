import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {AppService} from "../../../app.service";
import {CreateSupplier} from "./CreateSupplier";
import {SuppliersResponse} from "./interfaces/SuppliersResponse";

@Injectable({
    providedIn: 'root'
})
export class SuppliersService {

    constructor(private http: HttpClient, private appService: AppService) {
    }

    addSupplier(supplier: CreateSupplier): Observable<void> {
        return this.http.post<void>(`${this.appService.baseApi}/suppliers`, supplier);
    }

    getSubbliers(
        page: number,
        size?: number,
        name?: string,
        isActive: boolean =true
    ): Observable<SuppliersResponse> {
        let link = `${this.appService.baseApi}/suppliers?page=${page}`;
        if (size) {
            link += `&size=${size}`;
        }
        if (name) {
            link += `&name=${name}`;
        }
        link += `&isActive=${isActive}`;
        return this.http.get<SuppliersResponse>(link);
    }
}
