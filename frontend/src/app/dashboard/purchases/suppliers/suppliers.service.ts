import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {AppService} from "../../../app.service";
import {CreateSupplier} from "./CreateSupplier";

@Injectable({
    providedIn: 'root'
})
export class SuppliersService {

    constructor(private http: HttpClient, private appService: AppService) {
    }

    addSupplier(supplier: CreateSupplier): Observable<void> {
        return this.http.post<void>(`${this.appService.baseApi}/suppliers`, supplier);
    }
}
