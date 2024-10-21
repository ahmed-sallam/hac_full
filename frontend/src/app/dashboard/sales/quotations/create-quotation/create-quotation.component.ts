import {
  AsyncPipe,
  CurrencyPipe,
  DatePipe,
  DecimalPipe,
} from "@angular/common";
import { Component, inject, OnInit } from "@angular/core";
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from "@angular/forms";
import { Router } from "@angular/router";
import { Store } from "@ngrx/store";
import { catchError, map, Observable, of, switchMap } from "rxjs";
import { TranslatePipe } from "../../../../pipes/translate.pipe";
import { State } from "../../../../state/reducers";
import { LangState } from "../../../../state/reducers/lang.reducer";
import { selectLanguage } from "../../../../state/selectors/lang.selectors";
import { ToastService } from "../../../../toast/toast.service";
import { MainContentComponent } from "../../../components/main-content/main-content.component";
import { CurrencyService } from "../../../core/currency/currency.service";
import { Currency } from "../../../core/currency/interfaces/Currency";
import { SelectProductWithSearchComponent } from "../../../inventory/products/create-product/select-product-with-search/select-product-with-search.component";
import { SelectWithSearchComponent } from "../../../inventory/products/create-product/select-with-search/select-with-search.component";
import { ProductEntity } from "../../../inventory/products/interfaces/ListProductsResponse";
import { ProductsService } from "../../../inventory/products/products.service";
import { PaymentTerms } from "../../../purchases/rfpq/enums/PaymentTerms";
import { CustomersService } from "../../customers/customers.service";
import {
  CustomerEntity,
  CustomersResponse,
} from "../../customers/interfaces/CustomersResponse";
import { CreateQuotation } from "../interfaces/CreateQuotation";
import { QuotationsService } from "../quotations.service";

@Component({
  selector: "app-create-quotation",
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    TranslatePipe,
    ReactiveFormsModule,
    SelectProductWithSearchComponent,
    SelectWithSearchComponent,
    CurrencyPipe,
    DecimalPipe,
    DatePipe,
    FormsModule,
  ],
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
  quotationService: QuotationsService = inject(QuotationsService);
  productsService: ProductsService = inject(ProductsService);
  products$!: Observable<ProductEntity[]> | any;
  currencyService: CurrencyService = inject(CurrencyService);
  currencies$!: Observable<Currency[]>;
  cutomerService: CustomersService = inject(CustomersService);
  customers$!: Observable<CustomerEntity[]>;
  customer: number = 0;
  paymentTerms = PaymentTerms;
  paymentTermsKeys: string[] = [];

  formGroup: FormGroup = new FormGroup({
    date: new FormControl({ value: Date.now(), disabled: true }, [
      Validators.required,
    ]),
    validUntil: new FormControl("", [Validators.required]),
    customer: new FormControl("", [Validators.required]),
    discount: new FormControl(0, [Validators.required, Validators.min(0)]),
    paymentTerms: new FormControl("", [Validators.required]),
    currency: new FormControl("", [Validators.required]),
    notes: new FormControl(""),
    lines: this.fb.array([this.createLine()]),
    status: new FormControl("DRAFT"),
    vat: new FormControl(0, [Validators.required, Validators.min(0)]), // just for  ui
    subTotal: new FormControl(0, [Validators.required, Validators.min(0)]), // for ui
    total: new FormControl(0, [Validators.required, Validators.min(0)]), // for ui
  });
  currentDate: any = Date.now();

  ngOnInit(): void {
    this.getCurrencies();
    this.searchCustomers("");
    this.searchProduct("");
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
        })
      );
  }

  // addQuotation(data: CreateQuotation) {
  //   this.customer = data.customer;
  //   this.toastService.showSuccessToast();
  //   this.router.navigate(["/dashboard/sales/quotations"]);
  // }

  createLine(): FormGroup {
    return this.fb.group({
      quantity: new FormControl(1, [Validators.required, Validators.min(1)]),
      productId: new FormControl("", [Validators.required]),
      price: new FormControl(0, [Validators.required, Validators.min(0)]),
      discount: new FormControl(0, [Validators.required, Validators.min(0)]),
      total: new FormControl(0, [Validators.required, Validators.min(0)]), // for ui
      notes: new FormControl(""),
    });
  }

  get lines(): FormArray {
    return this.formGroup.get("lines") as FormArray;
  }
  getPaymentTermValue(key: string): string {
    return this.paymentTerms[key as keyof typeof PaymentTerms];
  }
  searchProduct($event: string) {
    const productIdInLines = this.lines.value.map((i: any) => i.productId);
    this.products$ = this.productsService.getProducts(0, 80, $event).pipe(
      switchMap((res) => {
        return of(
          res.content.filter(
            (product: ProductEntity) => !productIdInLines.includes(product.id)
          )
        );
      })
    );
  }
  addNewLine() {
    this.lines.push(this.createLine());
    this.searchProduct("");
  }
  removeLine(index: number) {
    this.lines.removeAt(index);
    this.products$ = null;
  }
  onLineSelected($event: any, index: number) {
    if ($event != null) {
      this.lines.at(index).get("productId")?.setValue($event.id);
    } else {
      this.lines.at(index).get("productId")?.setValue("");
    }
    this.searchProduct("");
  }

  onCustomerSelect($event: any) {
    if ($event == null) {
      this.formGroup.get("customer")?.setValue("");
    } else {
      this.formGroup.get("customer")?.setValue($event.id);
    }
  }
  changeTotals(index: number, changeVat: boolean = false) {
    this.changeLineTotal(index, changeVat);
    this.changeSubTotal();
    this.calcSetVat();
    this.setTotal();
  }
  changeLineTotal(index: number, changeVat: boolean = false) {
    const quantity = this.lines.at(index).get("quantity")?.value;
    const price = this.lines.at(index).get("price")?.value;
    const discount = this.lines.at(index).get("discount")?.value;
    const total = (price - discount) * quantity;
    this.lines.at(index).get("total")?.setValue(total);
  }

  changeSubTotal() {
    let subTotal = this.lines.controls
      .map((i) => i.get("total")?.value)
      .reduce((v, c) => v + c, 0);
    this.formGroup.get("subTotal")?.setValue(subTotal);
  }

  calcSetVat() {
    const subTotal = this.formGroup.get("subTotal")?.value | 0;
    const discount = this.formGroup.get("discount")?.value | 0;
    this.formGroup.get("vat")?.setValue((subTotal - discount) * 0.15);
  }

  changeDiscount() {
    this.calcSetVat();
    this.setTotal();
  }
  setTotal() {
    const subTotal = this.formGroup.get("subTotal")?.value | 0;
    const discount = this.formGroup.get("discount")?.value | 0;
    const vat = this.formGroup.get("vat")?.value | 0;
    const expenses = this.formGroup.get("totalExpenses")?.value | 0;
    const total = subTotal - discount + vat + expenses;
    this.formGroup.get("total")?.setValue(total);
  }

  onSubmitForm() {
    this.formGroup.markAllAsTouched();
    this.lines.markAllAsTouched();
    if (this.formGroup.invalid || this.lines.invalid) {
      console.log("errrr");
      //log all errors
      // Object.keys(this.formGroup.controls).forEach((key) => {
      //   const controlErrors = this.formGroup.get(key)?.errors;
      //   if (controlErrors != null) {
      //     Object.keys(controlErrors).forEach((keyError) => {
      //       console.log(
      //         "Key control: " +
      //           key +
      //           " -> keyError: " +
      //           keyError +
      //           " -> err value: ",
      //         controlErrors[keyError]
      //       );
      //     });
      //   }
      // });
      // Object.keys(this.lines.controls).forEach((key) => {
      //   const controlErrors = this.lines.get(key)?.errors;
      //   if (controlErrors != null) {
      //     Object.keys(controlErrors).forEach((keyError) => {
      //       console.log(
      //         "Key control: " +
      //           key +
      //           " -> keyError: " +
      //           keyError +
      //           " -> err value: ",
      //         controlErrors[keyError]
      //       );
      //     });
      //   }
      // });
    } else {
      console.log("sss");
      this.addQuotation(this.formGroup.value);
      // this.addQuotation(this.formGroup.value);
    }
  }

  addQuotation(data: CreateQuotation) {
    this.quotationService.createQuotation(data).subscribe({
      next: (res) => {
        this.toastService.showSuccessToast();
        this.router.navigate(["/dashboard/sales/quotations"]);
      },
      error: (err) => {
        this.toastService.showErrorToast();
      },
    });
  }
}
