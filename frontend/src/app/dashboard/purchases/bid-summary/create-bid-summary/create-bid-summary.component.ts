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
        QuotationsModalComponent
    ],
    templateUrl: './create-bid-summary.component.html',
    styles: `
        
    `

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
    products: Product[] = []
    suppliers: Supplier[] = []
    quotations:   any ;
    showHoverForItem:String='';
    showQuotationsModal: boolean = false;
    showQuotations: any;
    showSupplier: any;
    protected readonly Array = Array;

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
        this.suppliers = []
        this.products = []
        this.quotations = []
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

    loadAllQuotations(){
        this.resetData()
    this.supplierQuotationService.getSupplierQuotationsGrouped(this.formGroup.get("rfpqId")?.value, this.formGroup.get("fromDate")?.value).subscribe({
        next: (res) => {
            const sortedQuotations = res.quotations.sort((a: any, b: any) => {
                const A:any= Object.values(a)[0];
                const B:any = Object.values(b)[0];
                return A.date < B.date ? 1  :  A.date > B.date? -1 : 0;
            })
            this.quotations =  sortedQuotations.reduce((acc:QuotationS | any , item) => {
                const key = Object.keys(item)[0];
                if (!acc[key]) {
                    acc[key] = [];
                }
                acc[key].push(item[key]);
                return acc;
            }, {});
            this.products = res.products
            this.suppliers = res.suppliers
            console.log("quotations => ", this.quotations)

        }
    })
    }

    resetData(){
        this.products = []
        this.suppliers = []
        this.quotations = []
    }

    onItemHoverEnter(productNumber: string, id: number) {
        console.log("productNumber => ", productNumber + " id => " + id)
        this.showHoverForItem = id+'/'+ productNumber
    }

    onItemHoverLeave() {
        console.log("leave ",)
        this.showHoverForItem = ''
    }

    showQuotationFun(supplier: Supplier, quotation: any) {
        this.showQuotationsModal = true;
        this.showQuotations = quotation;
        this.showSupplier = supplier;
    }
    hideQuotationFun() {
        this.showQuotationsModal = false;
        this.showQuotations = null;
        this.showSupplier = null;
    }

    selectQuotationFun($event: any) {
        console.log("selectQuotationFun => ", $event)
    }
}
