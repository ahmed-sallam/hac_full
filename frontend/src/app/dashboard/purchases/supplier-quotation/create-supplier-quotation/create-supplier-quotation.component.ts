import {Component, inject} from '@angular/core';
import {AsyncPipe} from "@angular/common";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {Router} from "@angular/router";
import {ToastService} from "../../../../toast/toast.service";
import {
    FormArray,
    FormBuilder,
    FormControl,
    FormGroup,
    Validators
} from "@angular/forms";

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
    FormArray = FormArray;

    formGroup: FormGroup = new FormGroup({
        date: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        rfpq: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        supplier: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        subTotal: new FormControl(0, [Validators.required, Validators.min(0)]),
        discount: new FormControl(0, [Validators.required, Validators.min(0)]),
        vat: new FormControl(0, [Validators.required, Validators.min(0)]),
        totalExpenses: new FormControl(0, [Validators.required, Validators.min(0)]),
        total: new FormControl(0, [Validators.required, Validators.min(0)]),
        isLocal: new FormControl(true, [Validators.required]),
        paymentTerms: new FormControl('', [Validators.required]),
        supplierRef: new FormControl(''),
        notes: new FormControl(''),
        lines: this.fb.array([this.createLine()]),
        expenses: this.fb.array([this.createExpensesLine()])
    });

    createLine(): FormGroup {
        return this.fb.group({
            quantity: new FormControl(1, [Validators.required, Validators.min(1)]),
            product: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)]),
            price: new FormControl(0, [Validators.required, Validators.min(0)]),
            discount: new FormControl(0, [Validators.required, Validators.min(0)]),
            vat: new FormControl(0, [Validators.required, Validators.min(0)]),
            total: new FormControl(0, [Validators.required, Validators.min(0)]),
            notes: new FormControl(''),
        })
    }
    createExpensesLine(): FormGroup {
        return this.fb.group({
            expensesTitle: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)]),
            amount: new FormControl(0, [Validators.required, Validators.min(0.01)]),
        })
    }
}
