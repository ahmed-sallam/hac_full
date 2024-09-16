import { Component, inject, OnInit } from "@angular/core";
import {
  FormBuilder,
  FormArray,
  FormControl,
  FormGroup,
  Validators,
} from "@angular/forms";
import { Router } from "@angular/router";
import { Store } from "@ngrx/store";
import { catchError, map, Observable } from "rxjs";
import { LangState } from "../../../../state/reducers/lang.reducer";
import { selectLanguage } from "../../../../state/selectors/lang.selectors";
import { ToastService } from "../../../../toast/toast.service";
import { State } from "../../../../state/reducers";
import { CurrencyService } from "../../../core/currency/currency.service";
import { ProductEntity } from "../../../inventory/products/interfaces/ListProductsResponse";
import { ProductsService } from "../../../inventory/products/products.service";
import {
  CustomerEntity,
  CustomersResponse,
} from "../../customers/interfaces/CustomersResponse";
import { CustomersService } from "../../customers/customers.service";
import { Currency } from "../../../core/currency/interfaces/Currency";
import { PaymentTerms } from "../../../purchases/rfpq/enums/PaymentTerms";

@Component({
  selector: "app-create-quotation",
  standalone: true,
  imports: [],
  templateUrl: "./create-quotation.component.html",
  styles: ``,
})
export class CreateQuotationComponent implements OnInit {
  store: Store<State> = inject(Store<State>);

  selectLanguage$: Observable<LangState> = this.store.select(selectLanguage);
  router = inject(Router);
  toastService: ToastService = inject(ToastService);
  fb = inject(FormBuilder);
  FormArray = FormArray;

  productsService: ProductsService = inject(ProductsService);
  products$!: Observable<ProductEntity[]> | any;
  currencyService: CurrencyService = inject(CurrencyService);
  currencies$!: Observable<Currency[]>;
  cutomerService: CustomersService = inject(CustomersService);
  customers$!: Observable<CustomerEntity[]>;
  customerId: number = 0;
  paymentTerms = PaymentTerms;
  paymentTermsKeys: string[] = [];

  formGroup: FormGroup = new FormGroup({
    date: new FormControl("", [Validators.required]),
    validUntil: new FormControl("", [Validators.required]),
    customerId: new FormControl("", [Validators.required]),
    discount: new FormControl(0, [Validators.required, Validators.min(0)]),
    paymentTerms: new FormControl("", [Validators.required]),
    currency: new FormControl("", [Validators.required]),
    notes: new FormControl(""),
    lines: this.fb.array([this.createLine()]),
    status: new FormControl("DRAFT"),
  });

  ngOnInit(): void {
    this.getCurrencies();
    this.searchCustomers("");

    this.paymentTermsKeys = Object.keys(this.paymentTerms);
  }

  getCurrencies() {
    this.currencies$ = this.currencyService.getCurrencies();
  }

  searchCustomers(s: string) {
    this.customers$ = this.cutomerService
      .getCustomers(0, 80, s.trim(), true)
      .pipe(
        map((res: CustomersResponse) => {
          return res.content;
        }),
        catchError((err: any) => {
          return [];
        }),
      );
  }

  // addQuotation(data: CreateQuotation) {
  //   this.customerId = data.customerId;
  //   this.toastService.showSuccessToast();
  //   this.router.navigate(["/dashboard/sales/quotations"]);
  // }

  createLine(): FormGroup {
    return this.fb.group({
      quantity: new FormControl(1, [Validators.required, Validators.min(1)]),
      productId: new FormControl(0, [Validators.required]),
      price: new FormControl(0, [Validators.required, Validators.min(0)]),
      discount: new FormControl(0, [Validators.required, Validators.min(0)]),
      notes: new FormControl(""),
    });
  }

  // onSubmitForm() {
  //   this.formGroup.markAllAsTouched();
  //   if (this.formGroup.invalid || this.lines.invalid) {
  //   } else {
  //     this.addQuotation(this.formGroup.value);
  //   }
  // }
}
