import {Component, inject, OnInit} from "@angular/core";
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {AsyncPipe} from "@angular/common";
import {
    MainContentComponent
} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {
    HistorySectionComponent
} from "../../../components/history-section/history-section.component";
import {UserHistoryService} from "../../../user-history.service";
import {LoaderService} from "../../../components/loader/loader.service";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {History} from "../../material-request/interfaces/OneMaterialRequest";
import {LoaderComponent} from "../../../components/loader/loader.component";
import {BidSummaryService} from "../bid-summary.service";
import {OneBidSummaryResponse} from "../interfaces/OnBidSummaryResponse";
import {
    Supplier
} from "../../supplier-quotation/interfaces/SupplierQuotationsGrouped";

@Component({
    selector: "app-one-bid-summary",
    standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        TranslatePipe,
        RouterLink,
        HistorySectionComponent,
        LoaderComponent,
    ],
    templateUrl: "./one-bid-summary.component.html",
    styles: ``,
})
export class OneBidSummaryComponent implements OnInit {
    selectLanguage$!: Observable<LangState>;
    loaderService: LoaderService = inject(LoaderService);
    loader$!: Observable<boolean>;
    store: Store<State> = inject(Store<State>);
    userHistoryService: UserHistoryService = inject(UserHistoryService);
    userHistory: History[] = [];
    activeRouter: ActivatedRoute = inject(ActivatedRoute);
    bidsummaryId!: number;
    userHistoryLoading: boolean = false;
    bidSummaryService = inject(BidSummaryService);
    bidSummary!: OneBidSummaryResponse;
    showHoverForItem: string = "";

    showSupplier: any;
    showOneQuotationDetails: boolean = false;
    showOneQuotation!: any;
    selectedProducts: UpdateProduct[] | any = [];
    showProduct: any;
    showQuotationsModal: boolean = false;
    showQuotations: any;

    ngOnInit(): void {
        this.loader$ = this.loaderService.isLoading;
        this.loaderService.show();
        this.selectLanguage$ = this.store.select(selectLanguage);
        this.initPageParams();
        if (this.bidsummaryId) {
            this.getOneBidSummary();
            this.getHistory();
        }
    }

    getHistory() {
        this.userHistoryLoading = true;
        this.userHistoryService
            .getUserHistory("bid_summary", this.bidsummaryId)
            .subscribe({
                next: (data) => {
                    this.userHistory = data;
                    this.userHistoryLoading = false;
                },
                error: (err) => {
                    console.log(err);
                    this.userHistoryLoading = false;
                },
            });

        // this.userHistory$.subscribe(() => {
        // })
    }

    getOneBidSummary() {
        this.loaderService.show();

        this.bidSummaryService.getOneBidSummary(this.bidsummaryId).subscribe({
            next: (response: any) => {
                this.bidSummary = response;
                this.bidSummary.lines.forEach((line: any) => {
                    this.selectedProducts.push({
                        id: line.id,
                        quantity: line.quantity,
                        quotationId: line.quotationId,
                        productId: line.productId,
                        productNumber: this.bidSummary.generateBidSummary.products
                            .find((item: any) => item.id == line.productId)?.productNumber,
                        supplierId: line.supplierId,
                        price: line.price,
                        vat: line.vat,
                        total: line.total,
                    });
                });
                console.log("bidSummary => ", this.bidSummary);
                console.log("selectedProducts => ", this.selectedProducts);

                this.loaderService.hide();
            },
        });
    }

    onItemHoverEnter(productNumber: string, id: number) {
        console.log("productNumber => ", productNumber + " id => " + id);
        this.showHoverForItem = id + "/" + productNumber;
    }

    onItemHoverLeave() {
        console.log("leave ");
        this.showHoverForItem = "";
    }

    showOneQuotationDetailsFun(quotation: any, supplier: any, product?: any) {
        this.showOneQuotationDetails = true;
        this.showOneQuotation = quotation;
        this.showSupplier = supplier;
        this.showProduct = product;
    }

    showQuotationFun(supplier: Supplier, quotation: any, product?: any) {
        this.showQuotationsModal = true;
        this.showQuotations = quotation;
        this.showSupplier = supplier;
        this.showProduct = product;
    }

    selectQuotationProduct(product: any, supplier: any, quotation: any) {
        this.showProduct = product;
        this.showSupplier = supplier;
        this.selectQuotationFun(quotation);
    }

    selectQuotationFun($event: any) {
        // todo edit it according to updateProduct
        console.log("selectQuotationFun => ", $event);
        this.loaderService.show();
        this.selectedProducts = this.selectedProducts.filter(
            (item: any) => item.productNumber != $event.product.productNumber
        );
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
            vat: $event.vat,
            selectedProductId: $event.product.id,
            selectedProductNumber: $event.product.productNumber,
            selectedProductSubBrandAr: $event.product.subBrandAr,
            selectedProductSubBrandEn: $event.product.subBrandEn,
        });
        console.log("selectedProducts => ", this.selectedProducts);
        this.loaderService.hide();
    }

    getOneOfSelectedProduct($event: any): UpdateProduct {
        console.log("selectedProducts => ", this.selectedProducts);
        return this.selectedProducts.find(
            (item: UpdateProduct) => item.productNumber == $event.productNumber
        );
    }

    getOneSupplierProduct(key: string): any {
        const r = this.bidSummary.generateBidSummary.quotations.filter(
            (item) => item[key]
        );
        console.log("rrrr ", r);
        return r;
    }

    updateBidSummaryStatus(status: string) {
        console.log("change status => ", status);
        this.loaderService.show();
        this.bidSummaryService.updateBidSummaryStatus(this.bidsummaryId, status).subscribe({
            next: () => {
                //  this.bidSummaryService.getOneBidSummary(this.bidsummaryId);
                // this.loaderService.hide();
                this.getOneBidSummary();
                this.getHistory();
            },
            error: (err) => {
                console.log(err);
                this.loaderService.hide();
            },
        });
    }

    private initPageParams() {
        this.activeRouter.params.subscribe((params) => {
            this.bidsummaryId = params["id"];
        });
    }
}

export interface UpdateProduct {
    id: number;
    quantity: number;
    quotationId: number;
    productId: number;
    productNumber: string;
    supplierId: number;
    price: number;
    vat: number;
    total: number;
}
