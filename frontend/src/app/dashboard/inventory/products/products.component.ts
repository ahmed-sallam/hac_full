import {Component, inject, OnInit} from '@angular/core';
import {
    MainContentComponent
} from "../../components/main-content/main-content.component";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {
    SearchInputComponent
} from "../../components/search-input/search-input.component";
import {AsyncPipe, DatePipe, NgClass} from "@angular/common";
import {TranslatePipe} from "../../../pipes/translate.pipe";
import {Store} from "@ngrx/store";
import {State} from "../../../state/reducers";
import {ProductsService} from "./products.service";
import {selectLanguage} from "../../../state/selectors/lang.selectors";
import {Observable} from "rxjs";
import {LangState} from "../../../state/reducers/lang.reducer";
import {
    ListProductsResponse,
    ProductEntity
} from "./interfaces/ListProductsResponse";
import {Pageable} from "../stores/interfaces/StoreResponse";

@Component({
    selector: 'app-products',
    standalone: true,
    imports: [
        MainContentComponent,
        RouterLink,
        SearchInputComponent,
        DatePipe,
        TranslatePipe,
        AsyncPipe,
        NgClass
    ],
    templateUrl: './products.component.html',
    styles: ``
})
export class ProductsComponent implements OnInit {
    store: Store<State> = inject(Store<State>);
    activeRouter: ActivatedRoute = inject(ActivatedRoute);
    router: Router = inject(Router);
    productsService: ProductsService = inject(ProductsService)
    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage);
    products: ProductEntity[] = [];
    productsResponse!: ListProductsResponse;
    pageable!: Pageable;

    tableColumns: string[] = [
        'Number',
        'Brand',
        'Sub Brand',
        'Country',
        'Is Original',
        'Unit',
        'Stock',
        'Last updated',
        // 'Actions',
    ];
    currentPage!: number;
    pageSize!: number;
    searchName: string = '';

    ngOnInit(): void {
        this.initPageParams();
        this.getData();
    }

    getData() {
        this.productsService.getProducts(this.currentPage, this.pageSize, this.searchName)
            .subscribe((res) => {
                this.productsResponse = res;
                this.pageable = res.pageable;
                this.products = res.content;
            });
    }

    onSearchChanged($event: string) {
        this.searchName = $event;
        this.router.navigate([], {
            relativeTo: this.activeRouter,
            queryParams: {name: $event},
            queryParamsHandling: 'merge',
        });
        this.getData();
    }

    generatePageArray() {
        return new Array(this.productsResponse.totalPages);
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

    private initPageParams() {
        this.activeRouter.queryParams.subscribe((params) => {
            this.searchName = params['name'] || '';
            this.currentPage = params['page'] || 0;
            this.pageSize = params['size'] || 80;
        });
    }
}
