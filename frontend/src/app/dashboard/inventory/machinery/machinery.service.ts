import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppService} from "../../../app.service";
import {CreateBrand} from "../brands/interfaces/CreateBrand";
import {Observable} from "rxjs";
import {CreateMachineryType} from "./interfaces/CreateMachineryType";
import {BrandsResponse} from "../brands/BrandsResponse";
import {MachineryResponse, MachineryTypeEntity} from "./interfaces/MachineryResponse";
import {CreateMachineryModel} from "./interfaces/CreateMachineryModel";

@Injectable({
  providedIn: 'root'
})
export class MachineryService {

  constructor(private http: HttpClient, private appService: AppService) {}


  public addMachineryType(machineryType: CreateMachineryType): Observable<void> {
    return this.http.post<void>(`${this.appService.baseApi}/machinery`, machineryType);
  }

  public addMachineryModel(id: number, machineryModel: CreateMachineryModel): Observable<void> {
    return this.http.post<void>(`${this.appService.baseApi}/machinery/${id}/models`, machineryModel);
  }


  public updateMachineryType(id: number, machineryType: CreateMachineryType): Observable<void> {
    return this.http.put<void>(`${this.appService.baseApi}/machinery/${id}`, machineryType);
  }

  public updateMachineryModel(machineryId: number , id: number, machineryModel: CreateMachineryModel): Observable<void> {
    return this.http.put<void>(`${this.appService.baseApi}/machinery/${machineryId}/models/${id}`, machineryModel);
  }




  public getMachineryType(id: number): Observable<MachineryTypeEntity> {
    return this.http.get<MachineryTypeEntity>(`${this.appService.baseApi}/machinery/${id}`,);
  }
  public getMachinery(
      page: number,
      size?: number,
      name?: string,
      isActive: boolean =true
  ): Observable<MachineryResponse> {
    let link = `${this.appService.baseApi}/machinery?page=${page}`;
    if (size) {
      link += `&size=${size}`;
    }
    if (name) {
      link += `&name=${name}`;
    }
    link += `&isActive=${isActive}`;
    return this.http.get<MachineryResponse>(link);
  }
}
