import {Component, inject, OnInit} from '@angular/core';
import {AsyncPipe, CurrencyPipe, DecimalPipe} from "@angular/common";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {catchError, map, Observable, of, switchMap} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {Router} from "@angular/router";
import {ToastService} from "../../../../toast/toast.service";
import {
    FormArray,
    FormBuilder,
    FormControl,
    FormGroup,
    ReactiveFormsModule,
    Validators
} from "@angular/forms";
import {ProductsService} from "../../../inventory/products/products.service";
import {
    ProductEntity
} from "../../../inventory/products/interfaces/ListProductsResponse";
import {RfpqService} from "../../rfpq/rfpq.service";
import {RFPQResponse, RFPQShort} from "../../rfpq/interfaces/RFPQResponse";
import {SuppliersService} from "../../suppliers/suppliers.service";
import {
    SupplierEntity,
    SuppliersResponse
} from "../../suppliers/interfaces/SuppliersResponse";
import {PurchaseExpensesTitle, PurchaseService} from "../../purchase.service";
import {
    MainContentComponent
} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {
    SelectProductWithSearchComponent
} from "../../../inventory/products/create-product/select-product-with-search/select-product-with-search.component";
import {
    SelectWithSearchComponent
} from "../../../inventory/products/create-product/select-with-search/select-with-search.component";
import {PaymentTerms} from "../../rfpq/enums/PaymentTerms";

@Component({
    selector: 'app-create-supplier-quotation',
    standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        TranslatePipe,
        ReactiveFormsModule,
        SelectProductWithSearchComponent,
        SelectWithSearchComponent,
        CurrencyPipe,
        DecimalPipe
    ],
    templateUrl: './create-supplier-quotation.component.html',
    styles: ``
})
export class CreateSupplierQuotationComponent implements OnInit {
    store: Store<State> = inject(Store<State>)
    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage)
    router = inject(Router)
    toastService: ToastService = inject(ToastService)
    fb = inject(FormBuilder);
    FormArray = FormArray;
    productsService: ProductsService = inject(ProductsService)
    products$!: Observable<ProductEntity[]> | any;
    rfpqService: RfpqService = inject(RfpqService);
    rfpq$!: Observable<RFPQShort[]>;
    suppliersService: SuppliersService = inject(SuppliersService);
    suppliers$!: Observable<SupplierEntity[]>;
    purchaseService = inject(PurchaseService)
    purchaseExpensesTitle$!: Observable<PurchaseExpensesTitle[]>;

    paymentTerms = PaymentTerms;

    paymentTermsKeys: string[] = []

    formGroup: FormGroup = new FormGroup({
        date: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        rfpq: new FormControl('', [Validators.required]),
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
        expenses: this.fb.array([])
    });
    protected readonly PaymentTerms = PaymentTerms;

    get lines(): FormArray {
        return this.formGroup.get('lines') as FormArray;
    }

    get expenses(): FormArray {
        return this.formGroup.get('expenses') as FormArray;
    }

    createLine(): FormGroup {
        return this.fb.group({
            quantity: new FormControl(1, [Validators.required, Validators.min(1)]),
            product: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)]),
            price: new FormControl(0, [Validators.required, Validators.min(0)]),
            discount: new FormControl(0, [Validators.required, Validators.min(0)]),
            vat: new FormControl(0, [Validators.required, Validators.min(0)]),
            total: new FormControl(0, [Validators.required, Validators.min(0)]),
            notes: new FormControl(''),
            subTotal: new FormControl(0), // not send to Api just for ui
        })
    }

    createExpensesLine(): FormGroup {
        return this.fb.group({
            expensesTitle: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)]),
            amount: new FormControl(0, [Validators.required, Validators.min(0.01)]),
            notes: new FormControl('', []),
        })
    }

    ngOnInit(): void {
        this.searchRFPQ('')
        this.searchSuppliers('')
        this.searchProduct('')
        this.getExpensesTitles('')
        this.paymentTermsKeys = Object.keys(this.paymentTerms)
    }

    getPaymentTermValue(key: string): string {
        return this.paymentTerms[key as keyof typeof PaymentTerms]
    }

    getExpensesTitles(s: string) {
        this.purchaseExpensesTitle$ = this.purchaseService.getExpensesTitles()
            .pipe(map((res: PurchaseExpensesTitle[]) => {
                    return res
                }),
                catchError((err: any) => {
                    return []
                }))
    }

    searchProduct($event: string) {
        const productIdInLines = this.lines.value.map((i: any) => i.product);
        console.log(productIdInLines)
        this.products$ = this.productsService.getProducts(0, 80, $event)
            .pipe(
                switchMap((res) => {
                    return of(res.content.filter((product: ProductEntity) => !productIdInLines.includes(product.id)));
                })
            );

    }

    searchSuppliers(s: string) {
        this.suppliers$ = this.suppliersService.getSubbliers(0, 80,
            s.trim())
            .pipe(map((res: SuppliersResponse) => {
                    return res.content
                }),
                catchError((err: any) => {
                    return []
                }))
    }

    searchRFPQ(s: string) {
        this.rfpq$ = this.rfpqService.getRFPQs(0, undefined, 80,
            s.trim(), undefined, undefined
            , undefined, "REQUEST_FOR_P_QUOTATION", "PROCESSING")
            .pipe(map((res: RFPQResponse) => {
                    return res.content
                }),
                catchError((err: any) => {
                    return []
                }))
    }

    onSubmitForm() {
        this.formGroup.markAllAsTouched()
        this.lines.markAllAsTouched()
        this.expenses.markAllAsTouched()
        console.log("formGroupv", this.formGroup.value)
        console.log("formGroupe lines", this.formGroup.getError("lines"))
        console.log("formGroupe expenses", this.formGroup.getError("expenses"))

        if (this.formGroup.invalid || this.lines.invalid || this.expenses.invalid) {
            console.log("formGroup err", this.formGroup.errors)
        } else {
            console.log("formGroup", this.formGroup.value)
            // this.addSupplierQuotation(this.formGroup.value)
        }
    }

    addNewLine() {
        this.lines.push(this.createLine())
        this.searchProduct('')
    }

    addNewExpensesLine() {

        this.expenses.push(this.createExpensesLine())

    }

    removeLine(index: number) {
        this.lines.removeAt(index)
        this.products$ = null
    }

    removeExpensesLine(index: number) {
        this.expenses.removeAt(index)
    }

    onLineSelected($event: any, index: number) {

        console.log('item selected', $event)
        if ($event != null) {
            this.lines.at(index).get("product")?.setValue($event.id);
        } else {
            console.log(this.lines.value)
            this.lines.at(index).get('product')?.setValue('')
            console.log(this.lines.value)
        }
        this.products$ = null
    }

    // onExpensesLineSelected($event: any, index: number) {
    //     console.log('item selected', $event)
    //     if ($event != null) {
    //         this.expenses.at(index).get("expensesTitle")?.setValue($event.id);
    //     } else {
    //         console.log(this.expenses.value)
    //         this.expenses.at(index).get('expensesTitle')?.setValue('')
    //         console.log(this.expenses.value)
    //     }
    // }

    onRfpqSelect($event: any) {
        console.log("RFPQ ", $event)
        if ($event == null) {
            this.formGroup.get("rfpq")?.setValue('')
        } else {
            this.formGroup.get("rfpq")?.setValue($event.id)
        }
    }

    onSupplierSelect($event: any) {
        if ($event == null) {
            this.formGroup.get("supplier")?.setValue('')
        } else {
            this.formGroup.get("supplier")?.setValue($event.id)
        }
    }

    changeTotals(lineType: string, index: number, changeVat: boolean = false) {
        console.log("Outside if")
        if(lineType == "products") {
            console.log("in if")
            this.changeLineTotal(index, changeVat);
            this.changeSubTotal()
        }
    }

    changeLineTotal(index: number, changeVat: boolean = false) {
        const quantity = this.lines.at(index).get("quantity")?.value
        const price = this.lines.at(index).get("price")?.value
        const discount = this.lines.at(index).get("discount")?.value
        const vat = changeVat ? this.lines.at(index).get("vat")?.value : this.changeLineVat(index)
        const total = (price - discount + vat) * quantity;
        this.lines.at(index).get("total")?.setValue(total)

    }

    changeLineVat(index: number) {
        const price = this.lines.at(index).get("price")?.value
        const discount = this.lines.at(index).get("discount")?.value
        let vat =( price - discount) * 0.15;
        vat = parseFloat(parseFloat(String(vat)).toFixed(2))
        this.lines.at(index).get("vat")?.setValue(vat)
        return vat
    }

    changeSubTotal() {
        let subTotal = this.lines.controls.map(i => i.get("total")?.value).reduce((v, c)=> v+c, 0)
        console.log("subTotal ", this.lines.controls.map(i => i.get("total")?.value))
        this.formGroup.get("subTotal")?.setValue(subTotal)
    }

    changeVat(index: number) {
    }

    changeTotal(index: number) {
    }
}
