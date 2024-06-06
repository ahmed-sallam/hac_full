import {Injectable} from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {AppService} from "../../../app.service";
import {Observable} from "rxjs";
import {ReplenishmentRequest} from "./interfaces/ReplenishmentRequest";

@Injectable({
  providedIn: 'root'
})
export class ReplenishmintService {

  constructor(private http: HttpClient, private appService: AppService) {}
  public addReplenishment(r: ReplenishmentRequest): Observable<number> {
    const link = `${this.appService.baseApi}/material_requests`;
    return this.http.post<number>(link, r);
  }
}
