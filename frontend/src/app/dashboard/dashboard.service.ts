import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor() { }

  sidebarVisibility = new BehaviorSubject<boolean>(true);

  sidebarShow (){
    this.sidebarVisibility.next(true);
    }
    sidebarHide (){
      this.sidebarVisibility.next(false);
    }
}
