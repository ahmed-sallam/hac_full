import {Component} from '@angular/core';
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {LoaderService} from "../../../components/loader/loader.service";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {OneSaleOrder} from "../interfaces/OneSaleOrder";
import {OrdersService} from "../orders.service";
import {AsyncPipe} from "@angular/common";
import {
    MainContentComponent
} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {
    HistorySectionComponent
} from "../../../components/history-section/history-section.component";

@Component({
    selector: 'app-one-order',
    standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        TranslatePipe,
        RouterLink,
        HistorySectionComponent
    ],
    templateUrl: './one-order.component.html',
    styles: ``
})
export class OneOrderComponent {
    selectLanguage$!: Observable<LangState>;
    loader$!: Observable<boolean>;
    saleOrder!: OneSaleOrder;
    orderId!: number;
    tableColumns: string[] = [
        "#",
        "Product",
        "Quantity",
        "price",
        "Discount (%)",
        "Total",
        "Notes",
    ];

    constructor(
        private loaderService: LoaderService,
        private store: Store<State>,
        private activeRouter: ActivatedRoute,
        private orderService: OrdersService
    ) {
        this.loader$ = this.loaderService.isLoading;
        this.selectLanguage$ = this.store.select(selectLanguage);
    }

    ngOnInit(): void {
        this.initPageParams();
    }

    getData() {
        this.loaderService.show();

        this.orderService.getOneSaleOrder(this.orderId).subscribe({
            next: (response: OneSaleOrder) => {
                this.saleOrder = response;
                this.loaderService.hide();
            },
        });
    }

    updateOrderStatus(status: string) {
        this.orderService
            .updateSaleOrder(this.orderId, status)
            .subscribe({
                next: () => {
                    this.getData();
                },
            });
    }

    private initPageParams() {
        this.loaderService.show();
        this.activeRouter.params.subscribe((params) => {
            this.orderId = params["id"];
            this.orderId && this.getData();
        });
    }
}
