import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {SupplierQuotation} from "../interfaces/SupplierQuotation";
import {LoaderService} from "../../../components/loader/loader.service";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {ActivatedRoute} from "@angular/router";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {SupplierQuotationService} from "../supplier-quotation.service";
import {AsyncPipe} from "@angular/common";
import {
    MainContentComponent
} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";

@Component({
    selector: 'app-one-supplier-quotation',
    standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        TranslatePipe
    ],
    templateUrl: './one-supplier-quotation.component.html',
    styles: ``
})
export class OneSupplierQuotationComponent implements OnInit {
    selectLanguage$!: Observable<LangState>;
    loader$!: Observable<boolean>;
    tableColumns: string[] = [
        '#',
        'Product',
        'Quantity',
        'Price',
        'Discount',
        'Total',
        'Notes',
    ];
    supplierQuotation!: SupplierQuotation;
    sqId!: number;

    constructor(
        private loaderService: LoaderService,
        private store: Store<State>,
        private activeRouter: ActivatedRoute,
        private supplierQuotationService: SupplierQuotationService,
    ) {
        this.loader$ = this.loaderService.isLoading;
        this.selectLanguage$ = this.store.select(selectLanguage);
    }

    ngOnInit(): void {
        this.initPageParams();
    }

    getData() {
        this.loaderService.show()

        this.supplierQuotationService.getOne(this.sqId)
            .subscribe({
                next: (response: SupplierQuotation) => {
                    this.supplierQuotation = response;
                    this.loaderService.hide()
                }
            });
    }

    private initPageParams() {
        this.loaderService.show()
        this.activeRouter.params.subscribe(params => {
            this.sqId = params['id'];
            this.sqId && this.getData()
        })
    }
}
