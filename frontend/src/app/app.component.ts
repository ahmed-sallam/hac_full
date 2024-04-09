import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterOutlet} from '@angular/router';
import {Store} from "@ngrx/store";
import {State} from "./state/reducers";
import {selectLanguage} from "./state/selectors/lang.selectors";
import {ToastComponent} from "./toast/toast.component";
import {Observable} from "rxjs";
import {ToastService} from "./toast/toast.service";
import {initFlowbite} from "flowbite";

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [CommonModule, RouterOutlet, ToastComponent],
    templateUrl: './app.component.html',
    styles: [],
})
export class AppComponent implements OnInit {
    ngOnInit(): void {
        initFlowbite();
    }

    store = inject(Store<State>)
    toastService = inject(ToastService)
    selectLanguage$ = this.store.select(selectLanguage)
    showWarningToast$: Observable<boolean> = this.toastService.warning;
    showErrorToast$: Observable<boolean> = this.toastService.error;
    showSuccessToast$: Observable<boolean> = this.toastService.success;


}
