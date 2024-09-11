import {
  CustomerEntity,
  CustomersResponse,
} from "./interfaces/CustomersResponse";

import { AppService } from "../../../app.service";
import { CreateCustomer } from "./interfaces/CreateCustomer";
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({
  providedIn: "root",
})
export class CustomersService {
  constructor(private http: HttpClient, private appService: AppService) {}

  addCustomer(customer: CreateCustomer): Observable<void> {
    return this.http.post<void>(
      `${this.appService.baseApi}/customers`,
      customer
    );
  }

  getCustomers(
    page: number,
    size?: number,
    name?: string,
    isActive: boolean = true
  ): Observable<CustomersResponse> {
    let link = `${this.appService.baseApi}/customers?page=${page}`;
    if (size) {
      link += `&size=${size}`;
    }
    if (name) {
      link += `&name=${name}`;
    }
    link += `&isActive=${isActive}`;
    return this.http.get<CustomersResponse>(link);
  }

  getCustomerById(id: number): Observable<CustomerEntity> {
    return this.http.get<CustomerEntity>(
      `${this.appService.baseApi}/customers/${id}`
    );
  }

  updateCustomer(id: number, customer: CustomerEntity): Observable<void> {
    return this.http.put<void>(
      `${this.appService.baseApi}/customers/${id}`,
      customer
    );
  }
}
