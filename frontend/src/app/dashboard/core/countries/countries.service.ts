import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Country} from "./interfaces/Country";
import {HttpClient} from "@angular/common/http";
import {AppService} from "../../../app.service";

@Injectable({
  providedIn: 'root'
})
export class CountriesService {

  constructor(private http: HttpClient, private appService: AppService) {}
  getCountries() : Observable<Country[]> {
    return this.http.get<Country[]>(`${this.appService.baseApi}/countries`);
  }
}
