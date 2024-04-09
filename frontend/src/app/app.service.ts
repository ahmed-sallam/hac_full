import {Injectable} from '@angular/core';
import {environment} from "../environments/environment";

const apiUrl = environment.apiUrl;
@Injectable({
  providedIn: 'root',
})
export class AppService {
  constructor() {}

  // public baseApi = 'http://hacbackend:8080/api/v1';
  public baseApi = apiUrl;
}
