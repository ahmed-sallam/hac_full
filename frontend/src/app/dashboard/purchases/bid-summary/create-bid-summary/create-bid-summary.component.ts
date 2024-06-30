import {Component, inject, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {catchError, map, Observable} from "rxjs";
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
import {Line} from "../../material-request/interfaces/OneMaterialRequest";
import {
    SupplierQuotationService
} from "../../supplier-quotation/supplier-quotation.service";

type SupplierInfo = { id: number, nameAr: string, nameEn: string };

@Component({
    selector: 'app-create-bid-summary',
    standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        TranslatePipe,
        FormsModule,
        ReactiveFormsModule,
        SelectWithSearchComponent
    ],
    templateUrl: './create-bid-summary.component.html',
    styles: ``
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
    products: Line[] = []
    sortedQuotations: any[] = []
    suppliers: SupplierInfo[] = []
    quotations: any[] = []

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
        this.sortedQuotations = []
        this.suppliers = []
        this.products = []
        this.quotations = []
        if ($event == null) {
            this.formGroup.get("rfpqId")?.setValue('')
            // this.lines.clear()
            setTimeout(() => {
                // this.addNewLine()
            }, 1)
        } else {
            this.formGroup.get("rfpqId")?.setValue($event.id)
            this.getRfpqData();
        }
    }

    getRfpqData() {

        this.rfpqService.getOneRFPQ(this.formGroup.get("rfpqId")?.value).subscribe({
            next: (res) => {
                console.log(res)
                this.products = res.lines
            }
        })
    }

    getAllQuotations() {
        this.formGroup.get("fromDate")?.markAsTouched()
        this.formGroup.get("rfpqId")?.markAsTouched()
        if (this.formGroup.invalid) {
            return
        }
        this.sortedQuotations = []
        this.suppliers = []
        this.quotations = []
        this.products.forEach(async (line) => {
            const date = new Date(this.formGroup.get("fromDate")?.value)
            console.log("date => ", date.toISOString().split('T')[0])
            this.getOneQuotation(line.product.productNumber, date.toISOString().split('T')[0]).subscribe({
                next: (res) => {
                    this.sortedQuotations.push(res);
                },
            })
        });
        setTimeout(() => {
                    this.loadAllQuotations()
                    console.log("this.sortedQuotations ", this.sortedQuotations)
                    console.log("this.suppliers ", this.suppliers)
        }, this.products.length * 100)
    }

    getOneQuotation(productNumber: string, fromDate: string) {
        return this.supplierQuotationService.getQuotationsGroupedBySupplier(productNumber, fromDate)
            .pipe(
                map((res) => {
                        let data = {productNumber, supplierQuotations: []}
                        let supplierQuotations = res.map((item: any) => {
                            !this.suppliers.find((i: SupplierInfo) => i.id == item.id) && this.suppliers.push({
                                id: item.id,
                                nameAr: item.nameAr,
                                nameEn: item.nameEn
                            })
                            return {
                                ...item,
                                quotations: item.quotations.sort((a: any, b: any) => a.supplierQuotation.date > b.supplierQuotation.date ? -1 : a.supplierQuotation.date > b.supplierQuotation.date ? 1 : 0)
                            };
                        });
                        console.log("productNumber sorted => ", supplierQuotations);
                        return data = {...data, supplierQuotations};
                    }
                )
            )
    }

    loadAllQuotations() {
        this.products.forEach((line) => {
            this.suppliers.forEach((supplier) => {
                this.quotations.push(this.loadOneQuotation(line.product.productNumber, supplier.id));
            });
        });
    }

    loadOneQuotation(productNumber: string, supplierId: number) {
        let quotations = [];
        let product = this.sortedQuotations.find((item) => item.productNumber == productNumber)
        if (product) {
            let supplier = product.supplierQuotations.find((item: any) => item.id == supplierId)
            if (supplier) {
                console.log("supplier => ", supplier)
                quotations = supplier.quotations
                console.log("quotations => ", quotations)
            }
        }
        return quotations
    }

    onItemHoverEnter(productNumber: string, id: number) {
        console.log("productNumber => ", productNumber + " id => " + id)
    }

    onItemHoverLeave() {
        console.log("leave ",)
    }
}
