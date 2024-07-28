import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppService} from "../app.service";
import {Observable} from "rxjs";
import {
    History
} from "./purchases/material-request/interfaces/OneMaterialRequest";

@Injectable({
    providedIn: 'root'
})
export class UserHistoryService {

    constructor(private http: HttpClient, private appService: AppService) {
    }

    getUserHistory(tableName: string, id: number): Observable<History[]> {
        const link = `${this.appService.baseApi}/user-histories?id=${id}&tableName=${tableName}`;
        return this.http.get<History[]>(link);
    }
}
