import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  sidebarVisibility = new BehaviorSubject<boolean>(true);

  constructor() {

  }

  sidebarShow (){
    this.sidebarVisibility.next(true);
    }
    sidebarHide (){
      this.sidebarVisibility.next(false);
    }
}
