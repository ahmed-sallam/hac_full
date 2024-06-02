import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AppService} from "../../../app.service";
import {Observable} from "rxjs";
import {MaterialRequestResponse} from "./interfaces/MaterialRequestResponse";
import {OneMaterialRequest} from "./interfaces/OneMaterialRequest";

@Injectable({
    providedIn: 'root'
})
export class MaterialRequestService {

    constructor(private http: HttpClient, private appService: AppService) {
    }

    getMaterialRequests(
        page: number, sort?: string, size?: number,
        search?: string, ref?: number, store?: number,
        user?: number, phase?: string, status?: string): Observable<MaterialRequestResponse> {
        let link: string = `${this.appService.baseApi}/material_requests?page=${page}`;
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
        return this.http.get<MaterialRequestResponse>(link);
    }


    getOneMaterialRequest(id: number): Observable<OneMaterialRequest> {
        let link: string = `${this.appService.baseApi}/material_requests/${id}`;
        return this.http.get<OneMaterialRequest>(link);
    }

    updateMaterialRequestStatus(id: number, status : string): Observable<any> {
        const headers = new HttpHeaders({
            'Content-Type': 'application/json',
        });
        let  link: string = `${this.appService.baseApi}/material_requests/${id}`;
        return this.http.patch<any>(link, JSON.stringify(status), {
            headers,
            withCredentials: true
        });
    }

}
