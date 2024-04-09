import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ToastService {

  constructor() { }

  success  = new BehaviorSubject<boolean>(false);

    warning  = new BehaviorSubject<boolean>(false);

    error  = new BehaviorSubject<boolean>(false);

    showSuccessToast() {
      this.success.next(true);
      setTimeout(()=>{
        this.hideSuccessToast();
      }, 3000);
    }
    hideSuccessToast() {
      this.success.next(false);
    }
    showWarningToast() {
      this.warning.next(true);
      setTimeout(()=>{
        this.hideWarningToast();
      }, 3000);
    }
    hideWarningToast() {
      this.warning.next(false);
    }

    showErrorToast() {
      this.error.next(true);
      setTimeout(()=>{
        this.hideErrorToast();
      }, 3000);
    }
    hideErrorToast() {
      this.error.next(false);
    }


}
