import {Component, inject, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {ToastService} from "../../../../toast/toast.service";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {StockService} from "../stock.service";
import {BehaviorSubject, catchError, map, Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AsyncPipe} from "@angular/common";
import {MainContentComponent} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {SelectWithSearchComponent} from "../../products/create-product/select-with-search/select-with-search.component";
import {StoresService} from "../../stores/stores.service";
import {StoreEntity, StoreResponse} from "../../stores/interfaces/StoreResponse";
import {ProductsService} from "../../products/products.service";
import {ListProductsResponse, ProductEntity} from "../../products/interfaces/ListProductsResponse";

@Component({
    selector: 'app-create-stock',
    standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        TranslatePipe,
        ReactiveFormsModule,
        SelectWithSearchComponent
    ],
    templateUrl: './create-stock.component.html',
    styles: ``
})
export class CreateStockComponent implements OnInit {

    store: Store<State> = inject(Store<State>)
    stockService: StockService = inject(StockService)
    toastService: ToastService = inject(ToastService)
    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage)
    activeRouter: ActivatedRoute = inject(ActivatedRoute);
    router: Router = inject(Router)

    storeService: StoresService = inject(StoresService)
    storeOptions$!: Observable<StoreEntity[]>;
    productService: ProductsService = inject(ProductsService)
    productOptions$!: Observable<ProductEntity[]>
    selectedStore!: StoreEntity;
    selectedProduct!: ProductEntity | any;

    productNumber$ = new BehaviorSubject<string>('')

    ngOnInit(): void {
        this.initPageParams()
    }


    private initPageParams() {
        this.activeRouter.queryParams.subscribe((params) => {
            const productId = params['productId']
            if (productId) {
                this.getOneProduct(productId)
            }
        });
        this.getStores()
        this.getProducts()
    }


    formGroup: FormGroup = new FormGroup({
        productId: new FormControl('', [Validators.required
        ]),
        storeId: new FormControl('', [Validators.required]),
        locationId: new FormControl('', []),
        quantity: new FormControl(0, [Validators.required]),

    });


    onSubmitForm() {
        this.formGroup.markAllAsTouched()
        if (this.formGroup.invalid) {
            return;
        }
        this.stockService.addStock(this.formGroup.value).subscribe({
            next: () => {
                this.formGroup.reset()
                this.toastService.showSuccessToast()
                this.goBack()
            },
            error: (r) => {
                this.toastService.showErrorToast()

            }
        })
    }


    goBack() {
        // window.history.back();
        this.router.navigate(['/dashboard/inventory/stock'])
    }

    cancelCreateStore() {
        this.formGroup.reset();
        this.goBack()
    }

    getStores(
        page: number = 0,
        size: number = 15,
        name: string = '',
        isActive: boolean = true
    ) {
        this.storeOptions$ = this.storeService.getStores(
            page,
            size,
            name,
            isActive
        ).pipe(
            map((res: StoreResponse) => {
                return res.content
            }),
            catchError((err) => {
                return []
            })
        )
    }

    getProducts(
        page: number = 0,
        size: number = 15,
        name: string = '',
        isActive: boolean = true
    ) {
        this.productOptions$ = this.productService.getProducts(
            page,
            size,
            name,
            isActive
        ).pipe(
            map((res: ListProductsResponse) => {
                return res.content
            }),
            catchError((err) => {
                return []
            })
        )
    }

    getOneProduct(id: number) {
        this.productService.getOneProduct(id).subscribe({
            next: (res: ProductEntity) => {
                this.selectedProduct = res
                this.productNumber$.next(res.productNumber)
                this.formGroup.get("productId")?.setValue(id)
            }
        })

    }

    searchStores($event: string) {
        this.getStores(0, 15, $event.trim(), true);
    }

    searchProducts($event: string) {
        this.getProducts(0, 15, $event.trim(), true);
    }

    onItemSelected($event: any, key: string) {
        if ($event == null) {
            this.selectedProduct = $event
            this.productNumber$.next('')
            return
        }
        this.formGroup.get(key)?.setValue($event.id)
        if (key == 'storeId') {
            this.selectedStore = $event
        } else if (key == 'productId') {
            console.log("eee", $event)
            this.selectedProduct = $event
            this.productNumber$.next($event.productNumber)
        }
    }

}
