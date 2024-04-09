import {Component, inject, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../state/reducers";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {selectLanguage} from "../../../state/selectors/lang.selectors";
import {Observable} from "rxjs";
import {LangState} from '../../../state/reducers/lang.reducer';
import {Pageable} from "../stores/interfaces/StoreResponse";
import {StockEntity, StockResponse} from "./interfaces/StockResponse";
import {StockService} from "./stock.service";
import {AsyncPipe, NgClass} from "@angular/common";
import {MainContentComponent} from "../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../pipes/translate.pipe";
import {SearchInputComponent} from "../../components/search-input/search-input.component";

@Component({
    selector: 'app-stock',
    standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        TranslatePipe,
        RouterLink,
        SearchInputComponent,
        NgClass
    ],
    templateUrl: './stock.component.html',
    styles: ``
})
export class StockComponent implements OnInit {
    store: Store<State> = inject(Store<State>);
    activeRouter: ActivatedRoute = inject(ActivatedRoute);
    router: Router = inject(Router);
    stockService: StockService = inject(StockService)
    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage);


    stock: StockEntity[] = [];
    stockResponse!: StockResponse;
    pageable!: Pageable;

    tableColumns: string[] = [
        "Product Number",
        "Brand",
        "Sub Brand",
        "Country",
        "Store",
        "Location",
        "Quantity"
    ];
    currentPage!: number;
    pageSize!: number;
    productNumber: string = '';


    ngOnInit(): void {
        this.initPageParams();
        this.getData();
    }

    private initPageParams() {
        this.activeRouter.queryParams.subscribe((params) => {
            this.productNumber = params['productNumber'] || '';
            this.currentPage = params['page'] || 0;
            this.pageSize = params['size'] || 15;
        });
    }

    private getData() {
        this.stockService
            .getStock(this.currentPage, this.pageSize, this.productNumber)
            .subscribe((data) => {
                this.stockResponse = data;
                this.pageable = data.pageable;
                this.stock = data.content;
            });
    }


    onSearchChanged($event: string) {
        this.productNumber = $event;
        this.router.navigate([], {
            relativeTo: this.activeRouter,
            queryParams: {name: $event},
            queryParamsHandling: 'merge',
        });
        this.getData();
    }

    generatePageArray() {
        return new Array(this.stockResponse.totalPages);
    }

    onPageChanged($index: number) {
        if ($index == -1) {
            return
        }
        if (+this.currentPage == $index) {
            return
        }
        this.currentPage = $index;
        this.router.navigate([], {
            relativeTo: this.activeRouter,
            queryParams: {page: $index},
            queryParamsHandling: 'merge',
        });
        this.getData();
    }
}
