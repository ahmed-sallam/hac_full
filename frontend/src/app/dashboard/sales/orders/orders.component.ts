import {Component, inject, OnInit} from "@angular/core";
import {Store} from "@ngrx/store";
import {State} from "../../../state/reducers";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {Observable} from "rxjs";
import {LangState} from "../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../state/selectors/lang.selectors";
import {selectUser} from "../../../state/selectors/auth.selectors";
import {Pageable} from "../../inventory/brands/BrandsResponse";
import {OrdersService} from "./orders.service";
import {
    SaleOrderContent,
    SaleOrdersResponse
} from "./interfaces/SaleOrdersResponse";
import {AsyncPipe, CurrencyPipe, DatePipe} from "@angular/common";
import {
    MainContentComponent
} from "../../components/main-content/main-content.component";
import {
    PagenationComponent
} from "../../components/pagenation/pagenation.component";
import {
    SearchInputComponent
} from "../../components/search-input/search-input.component";
import {TranslatePipe} from "../../../pipes/translate.pipe";

@Component({
    selector: "app-orders",
    standalone: true,
    imports: [
        AsyncPipe,
        DatePipe,
        MainContentComponent,
        PagenationComponent,
        SearchInputComponent,
        TranslatePipe,
        RouterLink,
        CurrencyPipe
    ],
    templateUrl: "./orders.component.html",
    styles: ``,
})
export class OrdersComponent implements OnInit {
    store: Store<State> = inject(Store<State>);
    activeRouter: ActivatedRoute = inject(ActivatedRoute);
    router: Router = inject(Router);
    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage);
    selectUser$: Observable<any> = this.store.select(selectUser);
    pageable!: Pageable;
    ordersService: OrdersService = inject(OrdersService);
    saleOrdersResponse!: SaleOrdersResponse;
    orderContents: SaleOrderContent[] = [];
    tableColumns: string[] = [
        "Internal Ref",
        "SO number",
        "Date",
        "Customer",
        "Status",
        "Phase",
        "total",
        "User",
    ];

    currentPage!: number;
    pageSize!: number;
    searchName: string = "";
    sort!: string | undefined;
    ref!: number | undefined;
    user!: number | undefined;
    customer!: number | undefined;
    status!: string | undefined;
    all: boolean = true;

    ngOnInit(): void {
        this.initPageParams();
        this.getData();
    }

    getData() {
        this.ordersService
            .getOrders(
                this.currentPage,
                this.sort,
                this.pageSize,
                this.searchName,
                this.ref,
                this.user,
                this.status,
                this.customer,
            )
            .subscribe((res) => {
                this.saleOrdersResponse = res;
                this.pageable = res.pageable;
                this.orderContents = res.content;
            });
    }

    onSearchChanged($event: any) {
        this.searchName = $event["search"];
        const params = {...$event};
        this.router.navigate([], {
            relativeTo: this.activeRouter,
            queryParams: params,
            queryParamsHandling: "merge",
        });
        this.getData();
    }

    generatePageArray() {
        return new Array(this.saleOrdersResponse.totalPages);
    }

    onPageChanged($index: number) {
        if ($index == -1) {
            return;
        }
        if (+this.currentPage == $index) {
            return;
        }
        this.currentPage = $index;
        this.router.navigate([], {
            relativeTo: this.activeRouter,
            queryParams: {page: $index},
            queryParamsHandling: "merge",
        });
        this.getData();
    }

    resetParamsAndSearch() {
        this.searchName = "";
        this.currentPage = 0;
        this.pageSize = 80;
        this.sort = undefined;
        this.ref = undefined;
        this.user = undefined;
        this.status = undefined;
        this.customer = undefined;
    }

    getMyRequests(id: number) {
        this.user = id;
        this.all = false;
        this.onSearchChanged({search: "", user: id});
    }

    getAll() {
        this.resetParamsAndSearch();
        this.all = true;
        this.onSearchChanged({search: ""});
    }

    private initPageParams() {
        this.activeRouter.queryParams.subscribe((params) => {
            this.searchName = params["search"] || "";
            this.currentPage = params["page"] || 0;
            this.pageSize = params["size"] || 80;
            this.sort = params["sort"];
            this.ref = params["ref"];
            this.user = params["user"];
            this.status = params["status"];
            this.customer = params["customer"];
        });
    }
}
