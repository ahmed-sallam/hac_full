import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppService} from "../../../app.service";
import {Observable} from "rxjs";
import {BrandEntity, BrandsResponse} from "./BrandsResponse";
import {CreateBrand} from "./interfaces/CreateBrand";

@Injectable({
  providedIn: 'root'
})
export class BrandsService {

  constructor(private http: HttpClient, private appService: AppService) {}

  public getBrands(
      page: number,
      size?: number,
      name?: string,
      isActive: boolean =true
  ): Observable<BrandsResponse> {
    let link = `${this.appService.baseApi}/brands?page=${page}`;
    if (size) {
      link += `&size=${size}`;
    }
    if (name) {
      link += `&name=${name}`;
    }
    link += `&isActive=${isActive}`;
    return this.http.get<BrandsResponse>(link);
  }

  public getBrandById(id: number): Observable<BrandEntity> {
    return this.http.get<BrandEntity>(`${this.appService.baseApi}/brands/${id}`);
  }

  public addBrand(brand: CreateBrand): Observable<void> {
    return this.http.post<void>(`${this.appService.baseApi}/brands`, brand);
  }

  public updateBrand(id: number, brand: CreateBrand): Observable<void> {
    return this.http.put<void>(`${this.appService.baseApi}/brands/${id}`, brand);
  }
}
