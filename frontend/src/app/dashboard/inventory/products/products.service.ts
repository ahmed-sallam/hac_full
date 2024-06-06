import {Injectable} from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {AppService} from "../../../app.service";
import {Observable} from "rxjs";
import {CreateProduct} from "./interfaces/CreateProduct";
import {ListProductsResponse, ProductEntity} from "./interfaces/ListProductsResponse";

@Injectable({
    providedIn: 'root'
})
export class ProductsService {

    constructor(private http: HttpClient, private appService: AppService) {
    }

    getProducts(page: number, size?: number, name?: string, isActive: boolean = true): Observable<ListProductsResponse> {
        let link: string = `${this.appService.baseApi}/products?page=${page}`;
        if (size) {
            link += `&size=${size}`;
        }
        if (name) {
            link += `&name=${name}`;
        }
        link += `&isActive=${isActive}`;
        return this.http.get<ListProductsResponse>(link);
    }

    public addProduct(product: CreateProduct): Observable<any> {
        return this.http.post<any>(`${this.appService.baseApi}/products`, product);
    }
    getOneProduct(id: number): Observable<ProductEntity> {
        return this.http.get<any>(`${this.appService.baseApi}/products/${id}`);
    }
}
