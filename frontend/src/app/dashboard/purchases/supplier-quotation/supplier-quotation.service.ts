import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppService} from "../../../app.service";

@Injectable({
  providedIn: 'root'
})
export class SupplierQuotationService {

  constructor(private http: HttpClient, private appService: AppService) {}

}
