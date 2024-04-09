import {CreateStore, StoreEntity, StoreResponse} from './interfaces/StoreResponse';

import {AppService} from '../../../app.service';
import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {CreateLocation} from "./interfaces/CreateLocation";

@Injectable({
  providedIn: 'root',
})
export class StoresService {
  constructor(private http: HttpClient, private appService: AppService) {}

  public getStores(
    page: number,
    size?: number,
    name?: string,
    isActive: boolean =true
  ): Observable<StoreResponse> {
    let link = `${this.appService.baseApi}/stores?page=${page}`;
    if (size) {
      link += `&size=${size}`;
    }
    if (name) {
      link += `&name=${name}`;
    }
    link += `&isActive=${isActive}`;
    return this.http.get<StoreResponse>(link);
  }

  public addStore(store: CreateStore): Observable<void> {
    const link = `${this.appService.baseApi}/stores`;
    return this.http.post<void>(link, store);
  }

  public updateStore(id: number, store: CreateStore): Observable<void> {
    return this.http.put<void>(
      `${this.appService.baseApi}/stores/${id}`,
      store
    );
  }
  public getStore(id: number): Observable<StoreEntity> {
    return this.http.get<StoreEntity>(
      `${this.appService.baseApi}/stores/${id}`
    );
  }
  public addLocationToStore(id: number, location: CreateLocation): Observable<void> {
    return this.http.post<void>(
      `${this.appService.baseApi}/stores/${id}/locations`,
      location
    );
  }
    public updateLocationInStore(id: number, locationId: number, location: CreateLocation): Observable<void> {
        return this.http.put<void>(
        `${this.appService.baseApi}/stores/${id}/locations/${locationId}`,
        location
        );
    }
}
