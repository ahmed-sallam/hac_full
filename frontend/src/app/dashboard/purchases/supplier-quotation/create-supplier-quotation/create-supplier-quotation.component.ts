import {Component, inject} from '@angular/core';
import {AsyncPipe} from "@angular/common";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {Router} from "@angular/router";
import {ToastService} from "../../../../toast/toast.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-create-supplier-quotation',
  standalone: true,
  imports: [
    AsyncPipe
  ],
  templateUrl: './create-supplier-quotation.component.html',
  styles: ``
})
export class CreateSupplierQuotationComponent {
  store: Store<State> = inject(Store<State>)
  selectLanguage$: Observable<LangState> = this.store.select(selectLanguage)
  router = inject(Router)
  toastService: ToastService = inject(ToastService)
  fb = inject(FormBuilder);
  formGroup: FormGroup = new FormGroup({
    date: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
    store: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
    notes: new FormControl(''),
    // lines: this.fb.array([this.createLine()])
  });
}
