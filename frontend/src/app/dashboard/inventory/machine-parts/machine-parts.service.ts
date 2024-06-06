import {Injectable} from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {AppService} from "../../../app.service";
import {Observable} from "rxjs";
import {MachinePartEntity, MachinePartResponse} from "./interfaces/MachinePartResponse";
import {CreateMachinePart} from "./interfaces/CreateMachinePart";

@Injectable({
  providedIn: 'root'
})
export class MachinePartsService {

  constructor(private http: HttpClient, private appService: AppService) {}

  public getMachineParts(
      page: number,
      size?: number,
      name?: string,
      isActive: boolean =true
  ): Observable<MachinePartResponse> {
    let link = `${this.appService.baseApi}/machine-parts?page=${page}`;
    if (size) {
      link += `&size=${size}`;
    }
    if (name) {
      link += `&name=${name}`;
    }
    link += `&isActive=${isActive}`;
    return this.http.get<MachinePartResponse>(link);
  }
  addMachinePart(machinePart: CreateMachinePart): Observable<any> {
    return this.http.post<any>(`${this.appService.baseApi}/machine-parts`, machinePart);
  }
  getMachinePart(id: number): Observable<MachinePartEntity> {
    return this.http.get<MachinePartEntity>(`${this.appService.baseApi}/machine-parts/${id}`);
  }
  updateMachinePart( id: number, machinePart: CreateMachinePart): Observable<any> {
    return this.http.put<any>(`${this.appService.baseApi}/machine-parts/${id}`, machinePart);
  }

}
