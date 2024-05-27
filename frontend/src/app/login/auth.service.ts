import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppService} from "../app.service";
import {Observable} from "rxjs";
import {LoginResponse} from "./interfaces/LoginResponse";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private appService: AppService) { }

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.appService.baseApi}/auth/login`, { username, password });
  }
  // logout(): Observable<any> {
  //   return this.http.post(`${this.appService.baseApi}/auth/logout`, {});
  // }
}
