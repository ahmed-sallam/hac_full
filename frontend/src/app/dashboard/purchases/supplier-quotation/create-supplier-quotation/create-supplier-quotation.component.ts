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
    FormsModule,
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
import {Currency} from "../../../core/currency/interfaces/Currency";
import {CurrencyService} from "../../../core/currency/currency.service";
import {ReceiveIn} from "../enums/ReceiveIn";
import {SupplierQuotationService} from "../supplier-quotation.service";
import {CreateSupplierQuotation} from "../interfaces/CreateSupplierQuotation";

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
        DecimalPipe,
        FormsModule
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
    supplierQuotationService: SupplierQuotationService = inject(SupplierQuotationService)
    productsService: ProductsService = inject(ProductsService)
    products$!: Observable<ProductEntity[]> | any;
    rfpqService: RfpqService = inject(RfpqService);
    currencyService: CurrencyService = inject(CurrencyService);
    rfpq$!: Observable<RFPQShort[]>;
    currencies$!: Observable<Currency[]>;
    suppliersService: SuppliersService = inject(SuppliersService);
    suppliers$!: Observable<SupplierEntity[]>;
    purchaseService = inject(PurchaseService)
    purchaseExpensesTitle$!: Observable<PurchaseExpensesTitle[]>;

    paymentTerms = PaymentTerms;
    receiveIn = ReceiveIn;

    paymentTermsKeys: string[] = []
    receiveInKeys: string[] = []

    formGroup: FormGroup = new FormGroup({
        date: new FormControl('', [Validators.required]),
        validTo: new FormControl('', [Validators.required]),
        rfpqId: new FormControl('', [Validators.required]),
        supplierId: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        subTotal: new FormControl(0, [Validators.required, Validators.min(0)]),
        discount: new FormControl(0, [Validators.required, Validators.min(0)]),
        vat: new FormControl(0, [Validators.required, Validators.min(0)]),
        totalExpenses: new FormControl(0, [Validators.required, Validators.min(0)]),
        total: new FormControl(0, [Validators.required, Validators.min(0)]),
        isLocal: new FormControl(true, [Validators.required]),
        paymentTerms: new FormControl('', [Validators.required]),
        receiveIn: new FormControl('', [Validators.required]),
        currencyId: new FormControl('', [Validators.required]),
        supplierRef: new FormControl(''),
        notes: new FormControl(''),
        lines: this.fb.array([this.createLine()]),
        expenses: this.fb.array([])
    });
    vatTerms: "15" | "0" = "15";

    get lines(): FormArray {
        return this.formGroup.get('lines') as FormArray;
    }

    get expenses(): FormArray {
        return this.formGroup.get('expenses') as FormArray;
    }

    createLine(): FormGroup {
        return this.fb.group({
            quantity: new FormControl(1, [Validators.required, Validators.min(1)]),
            productId: new FormControl(0, [Validators.required]),
            price: new FormControl(0, [Validators.required, Validators.min(0)]),
            discount: new FormControl(0, [Validators.required, Validators.min(0)]),
            // vat: new FormControl(0, [Validators.required, Validators.min(0)]),
            total: new FormControl(0, [Validators.required, Validators.min(0)]),
            // subTotal: new FormControl(0, [Validators.required, Validators.min(0)]),
            notes: new FormControl(''),
        })
    }

    createExpensesLine(): FormGroup {
        return this.fb.group({
            expensesTitleId: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)]),
            amount: new FormControl(0, [Validators.required, Validators.min(0.01)]),
            notes: new FormControl('', []),
        })
    }

    ngOnInit(): void {
        this.getCurrencies()
        this.searchRFPQ('')
        this.searchSuppliers('')
        this.searchProduct('')
        this.getExpensesTitles('')
        this.paymentTermsKeys = Object.keys(this.paymentTerms)
        this.receiveInKeys = Object.keys(this.receiveIn)
    }

    getCurrencies() {
        this.currencies$ = this.currencyService.getCurrencies()
    }

    getPaymentTermValue(key: string): string {
        return this.paymentTerms[key as keyof typeof PaymentTerms]
    }

    getReceiveIn(key: string): string {
        return this.receiveIn[key as keyof typeof ReceiveIn]
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
        const productIdInLines = this.lines.value.map((i: any) => i.productId);
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
            , undefined, "SUPPLIER_QUOTATION", "PROCESSING")
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
        if (this.formGroup.invalid || this.lines.invalid || this.expenses.invalid) {

        } else {
            this.addSupplierQuotation(this.formGroup.value)
        }
    }

    addSupplierQuotation(data: CreateSupplierQuotation) {
        this.supplierQuotationService.addSupplierQuotation(data)
            .subscribe((res) => {
                this.toastService.showSuccessToast()
                this.router.navigate(['/dashboard/purchases/supplier-quotations'])
            }, (err) => {
                this.toastService.showErrorToast()
            })
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
        if ($event != null) {
            this.lines.at(index).get("productId")?.setValue($event.id);
        } else {
            this.lines.at(index).get('productId')?.setValue('')
        }
        this.searchProduct('')
    }

    // onExpensesLineSelected($event: any, index: number) {
    //     } else {
    //         }
    // }

    onRfpqSelect($event: any) {
        if ($event == null) {
            this.formGroup.get("rfpqId")?.setValue('')
            // this.formGroup.get("lines")?.setValue([]);
            this.lines.clear()
            setTimeout(()=>{
                this.addNewLine()
        }, 1)
        } else {
            this.formGroup.get("rfpqId")?.setValue($event.id)
            this.getRfpqData();
        }
    }

    onSupplierSelect($event: any) {
        if ($event == null) {
            this.formGroup.get("supplierId")?.setValue('')
        } else {
            this.formGroup.get("supplierId")?.setValue($event.id)
        }
    }

    changeTotals(lineType: string, index: number, changeVat: boolean = false) {
        if (lineType == "products") {
            this.changeLineTotal(index, changeVat);
            this.changeSubTotal()
            this.calcSetVat()
            this.setTotal()
        }
    }

    changeLineTotal(index: number, changeVat: boolean = false) {
        const quantity = this.lines.at(index).get("quantity")?.value
        const price = this.lines.at(index).get("price")?.value
        const discount = this.lines.at(index).get("discount")?.value
        // const subTotal = (price - discount) * quantity;
        // this.lines.at(index).get("subTotal")?.setValue(subTotal)
        // const vat = changeVat ? this.lines.at(index).get("vat")?.value  : this.changeLineVat(index, price, discount, quantity)
        const total = (price - discount) * quantity;
        this.lines.at(index).get("total")?.setValue(total)

    }

    // changeLineVat(index: number, price: number = 0, discount: number = 0, quantity: number = 1) {
    //     let vat =( price - discount) * 0.15 * quantity
    //     vat = parseFloat(parseFloat(String(vat)).toFixed(2))
    //     this.lines.at(index).get("vat")?.setValue(vat)
    //     return vat
    // }

    changeSubTotal() {
        let subTotal = this.lines.controls.map(i => i.get("total")?.value).reduce((v, c) => v + c, 0)
        this.formGroup.get("subTotal")?.setValue(subTotal)
    }


    calcSetVat() {
        if (this.vatTerms == "15") {
            const subTotal = this.formGroup.get("subTotal")?.value | 0;
            const discount = this.formGroup.get("discount")?.value | 0;
            this.formGroup.get("vat")?.setValue((subTotal - discount) * 0.15)
        } else {
            this.formGroup.get("vat")?.setValue(0)
        }
    }

    changeDiscount() {
        this.calcSetVat()
        this.setTotal()
    }

    changeVatTerms(event: any) {
        this.vatTerms = event.target.value
        this.calcSetVat()
        this.setTotal()
        }

    setTotal() {
        const subTotal = this.formGroup.get("subTotal")?.value | 0;
        const discount = this.formGroup.get("discount")?.value | 0;
        const vat = this.formGroup.get("vat")?.value | 0;
        const expenses = this.formGroup.get("totalExpenses")?.value | 0;
        const total = subTotal - discount + vat + expenses
        this.formGroup.get("total")?.setValue(total)
    }

    getRfpqData() {
        this.lines.clear()
        this.rfpqService.getOneRFPQ(this.formGroup.get("rfpqId")?.value).subscribe({
            next: (res) => {

                res.lines.forEach((line: any) => {
                    const v = line.product.productNumber +' '+( true? (line.product.mainBrandAr + ' - '+ line.product.subBrandAr  ) : (line.product.mainBrandEn + ' - '+ line.product.subBrandEn ))
                    this.lines.push(this.fb.group({
                        quantity: new FormControl(line.quantity, [Validators.required, Validators.min(1)]),
                        productId: new FormControl(line.product.id, [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)]),
                        price: new FormControl(0, [Validators.required, Validators.min(0)]),
                        discount: new FormControl(0, [Validators.required, Validators.min(0)]),
                        total: new FormControl(0, [Validators.required, Validators.min(0)]),
                        notes: new FormControl(""),
                        v: new FormControl(v) // Just for ui
                    }))
                })
            }
        })
    }
}
