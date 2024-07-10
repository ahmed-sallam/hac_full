import {Component, inject, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {BehaviorSubject, catchError, map, Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {Router} from "@angular/router";
import {AsyncPipe} from "@angular/common";
import {
    MainContentComponent
} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {
    FormControl,
    FormGroup,
    FormsModule,
    ReactiveFormsModule,
    Validators
} from "@angular/forms";
import {
    SelectWithSearchComponent
} from "../../../inventory/products/create-product/select-with-search/select-with-search.component";
import {RFPQResponse, RFPQShort} from "../../rfpq/interfaces/RFPQResponse";
import {RfpqService} from "../../rfpq/rfpq.service";
import {
    SupplierQuotationService
} from "../../supplier-quotation/supplier-quotation.service";
import {
    Product,
    QuotationS,
    Supplier
} from "../../supplier-quotation/interfaces/SupplierQuotationsGrouped";
import {
    QuotationsModalComponent
} from "../quotations-modal/quotations-modal.component";
import {
    PurchaseQuotationModalComponent
} from "../purchase-quotation-modal/purchase-quotation-modal.component";
import {LoaderService} from "../../../components/loader/loader.service";
import {LoaderComponent} from "../../../components/loader/loader.component";
import {BidSummaryService} from "../bid-summary.service";


@Component({
    selector: 'app-create-bid-summary',
    standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        TranslatePipe,
        FormsModule,
        ReactiveFormsModule,
        SelectWithSearchComponent,
        QuotationsModalComponent,
        PurchaseQuotationModalComponent,
        LoaderComponent
    ],
    templateUrl: './create-bid-summary.component.html',
    styles: `
        input[type="number"]::-webkit-inner-spin-button,
        input[type="number"]::-webkit-outer-spin-button {
            -webkit-appearance: none;
            margin: 0;
        }

        input[type="number"] {
            -moz-appearance: textfield;
        }`
})
export class CreateBidSummaryComponent implements OnInit {
    store: Store<State> = inject(Store<State>)
    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage)
    router: Router = inject(Router)
    supplierQuotationService = inject(SupplierQuotationService)
    rfpq$!: Observable<RFPQShort[]>;
    formGroup: FormGroup = new FormGroup({
        fromDate: new FormControl("", [Validators.required]),
        rfpqId: new FormControl("", [Validators.required]),
    })
    rfpqService: RfpqService = inject(RfpqService);
    loaderService = inject(LoaderService)
    bidSummaryService: BidSummaryService = inject(BidSummaryService)
    products: Product[] = []
    suppliers: Supplier[] = []
    quotations: any;
    showHoverForItem: String = '';
    showQuotationsModal: boolean = false;
    showQuotations: any;
    showSupplier: any;
    showOneQuotationDetails: boolean = false;
    showOneQuotation!: any;
    selectedProducts: SelectedProduct[] | any = [];
    showProduct: any;
    isLoading: BehaviorSubject<boolean> = this.loaderService.isLoading;

    ngOnInit(): void {
        this.searchRFPQ('')
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

    onRfpqSelect($event: any) {
        this.resetData();
        // this.sortedQuotationsOld = []
        // this.suppliersOld = []
        // this.productsOld = []
        // this.quotationsOld = []
        if ($event == null) {
            this.formGroup.get("rfpqId")?.setValue('')
            // this.lines.clear()
            // setTimeout(() => {
            //     // this.addNewLine()
            // }, 1)
        } else {
            this.formGroup.get("rfpqId")?.setValue($event.id)
            // this.getRfpqData();
        }
    }

    getAllQuotations() {
        this.formGroup.get("fromDate")?.markAsTouched()
        this.formGroup.get("rfpqId")?.markAsTouched()
        if (this.formGroup.invalid) {
            return
        }
        this.loadAllQuotations()
    }

    loadAllQuotations() {
        this.resetData()
        this.loaderService.show()
        this.supplierQuotationService.getSupplierQuotationsGrouped(this.formGroup.get("rfpqId")?.value, this.formGroup.get("fromDate")?.value).subscribe({
            next: (res) => {
                const sortedQuotations = res.quotations.sort((a: any, b: any) => {
                    const A: any = Object.values(a)[0];
                    const B: any = Object.values(b)[0];
                    return A.date < B.date ? 1 : A.date > B.date ? -1 : 0;
                })
                this.quotations = sortedQuotations.reduce((acc: QuotationS | any, item) => {
                    const key = Object.keys(item)[0];
                    if (!acc[key]) {
                        acc[key] = [];
                    }
                    acc[key].push(item[key]);
                    return acc;
                }, {});
                this.products = res.products.map((item: any) => {
                    return {
                        ...item,
                        selectedQuantity: item.quantity
                    }
                })
                this.suppliers = res.suppliers
                console.log("quotations => ", this.quotations)
                console.log("products => ", this.products)
                this.loaderService.hide()
            } // todo: handle error
        })
    }

    resetData() {
        this.products = []
        this.suppliers = []
        this.quotations = []
        this.selectedProducts = []
        this.showHoverForItem = ''
    }

    onItemHoverEnter(productNumber: string, id: number) {
        console.log("productNumber => ", productNumber + " id => " + id)
        this.showHoverForItem = id + '/' + productNumber
    }

    onItemHoverLeave() {
        console.log("leave ",)
        this.showHoverForItem = ''
    }

    showQuotationFun(supplier: Supplier, quotation: any, product?: any) {
        this.showQuotationsModal = true;
        this.showQuotations = quotation;
        this.showSupplier = supplier;
        this.showProduct = product;
    }

    showOneQuotationDetailsFun(quotation: any, supplier: any, product?: any) {
        this.showOneQuotationDetails = true;
        this.showOneQuotation = quotation;
        this.showSupplier = supplier;
        this.showProduct = product;
    }

    hideQuotationFun() {
        this.showQuotationsModal = false;
        this.showQuotations = null;
        // to let select collect product & supplier data
        setTimeout(() => {
            this.showSupplier = null;
            this.showProduct = null;
        }, 500)
    }

    selectQuotationFun($event: any) {
        console.log("selectQuotationFun => ", $event)
        this.loaderService.show()
        this.selectedProducts = this.selectedProducts.filter((item: any) => item.productNumber != $event.product.productNumber)
        this.selectedProducts.push({
            productId: this.showProduct.id,
            productNumber: this.showProduct.productNumber,
            quantity: this.showProduct.quantity,
            selectedQuantity: this.showProduct.selectedQuantity,
            selectedQuotationId: $event.id,
            selectedQuotationDate: $event.date,
            selectedSupplierId: this.showSupplier.id,
            selectedSupplierNameAr: this.showSupplier.nameAr,
            selectedSupplierNameEn: this.showSupplier.nameEn,
            netPrice: $event.netPrice,
            currencyCode: $event.currency.code,
            sarPrice: $event.sarPrice,
            selectedProductId: $event.product.id,
            selectedProductNumber: $event.product.productNumber,
            selectedProductSubBrandAr: $event.product.subBrandAr,
            selectedProductSubBrandEn: $event.product.subBrandEn
        })
        console.log("selectedProducts => ", this.selectedProducts)
        this.loaderService.hide()
    }

    selectQuotationProduct(product: any, supplier: any, quotation: any) {
        this.showProduct = product;
        this.showSupplier = supplier;
        this.selectQuotationFun(quotation)
    }

    hideOneQuotationDetailsFun() {
        this.showOneQuotationDetails = false;
        this.showSupplier = null;
        this.showOneQuotation = null;
    }

    getOneOfSelectedProduct($event: any): SelectedProduct {
        return this.selectedProducts.find((item: any) => item.selectedProductNumber == $event.productNumber)
    }

    setProdcutQuantity($event: Event, product: Product) {
        this.products = this.products.map((item: Product) => {
            if (item.id == product.id) {
                item.selectedQuantity = Number(($event.target as HTMLInputElement).value)
            }
            return item
        })

        this.selectedProducts = this.selectedProducts.map((item: SelectedProduct) => {
                if (item.selectedProductNumber == product.productNumber) {
                    item.selectedQuantity = Number(($event.target as HTMLInputElement).value)
                }
                return item

            }
        )
    }

    cancelCreateBidSummary() {
        this.goToBidSummary()
    }

    onSubmitForm(confirm = false) {
        this.formGroup.markAllAsTouched()
        if (this.formGroup.invalid) {
            return
        }
        const data = {
            rfpqId: this.formGroup.get("rfpqId")?.value,
            fromDate: this.formGroup.get("fromDate")?.value,
            lines: this.selectedProducts.map((item: any) => {
                return {
                    productId: item.selectedProductId,
                    quantity: item.selectedQuantity,
                    quotationId: item.selectedQuotationId,
                    supplierId: item.selectedSupplierId
                }
            })
        }

        this.addBidSummary(data, confirm)
    }

    addBidSummary(r
                      :
                      any, confirm
                      :
                      boolean = false
    ) {
        this.loaderService.show()
        confirm ? r.status = "PENDING" : "DRAFT"
        this.bidSummaryService.addBidSummary(r).subscribe({
            next: (res: any) => {
                this.loaderService.hide()
                this.goToBidSummary()
            }
        })
    }

    goToBidSummary() {
        this.router.navigate(["/dashboard/purchases/bid-summaries"]);
    }
}

interface SelectedProduct {
    productId: number
    productNumber: string
    quantity: number
    selectedQuantity: number
    selectedQuotationId: number
    selectedQuotationDate: string
    selectedSupplierId: number
    selectedSupplierNameAr: string
    selectedSupplierNameEn: string
    netPrice: number
    currencyCode: string
    sarPrice: number
    selectedProductId: number
    selectedProductNumber: string
    selectedProductSubBrandAr: string
    selectedProductSubBrandEn: string
}