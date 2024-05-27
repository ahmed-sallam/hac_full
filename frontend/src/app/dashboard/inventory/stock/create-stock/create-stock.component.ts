import {Component, inject, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {ToastService} from "../../../../toast/toast.service";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {StockService} from "../stock.service";
import {BehaviorSubject, catchError, map, Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {
    FormControl,
    FormGroup,
    ReactiveFormsModule,
    Validators
} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AsyncPipe} from "@angular/common";
import {
    MainContentComponent
} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {
    SelectWithSearchComponent
} from "../../products/create-product/select-with-search/select-with-search.component";
import {StoresService} from "../../stores/stores.service";
import {
    StoreEntity,
    StoreResponse
} from "../../stores/interfaces/StoreResponse";
import {ProductsService} from "../../products/products.service";
import {
    ListProductsResponse,
    ProductEntity
} from "../../products/interfaces/ListProductsResponse";
import {
    SelectProductWithSearchComponent
} from "../../products/create-product/select-product-with-search/select-product-with-search.component";
import {StockResponseShort} from "../../products/interfaces/StockResponseShort";

@Component({
    selector: 'app-create-stock',
    standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        TranslatePipe,
        ReactiveFormsModule,
        SelectWithSearchComponent,
        SelectProductWithSearchComponent
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
    storeOptionsNotFiltered: StoreEntity[] = [];
    storeOptions: StoreEntity[] = [];
    productService: ProductsService = inject(ProductsService)
    productOptions$!: Observable<ProductEntity[]>
    selectedStore!: StoreEntity;
    selectedProduct!: ProductEntity | any;
    showStoreLocationQuantityInput: boolean = false;

    productNumber$ = new BehaviorSubject<string>('')
    // storeId$ = new BehaviorSubject<string>('')
    // locationId$ = new BehaviorSubject<string>('')
     showLocationInput: boolean = true;
     showStoreInput: boolean = true;
    formGroup: FormGroup = new FormGroup({
        productId: new FormControl('', [Validators.required
        ]),
        storeId: new FormControl('', [Validators.required]),
        locationId: new FormControl('', []),
        quantity: new FormControl(0, [Validators.required]),

    });

    ngOnInit(): void {
        this.initPageParams()
    }

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

    cancelCreateStock() {
        this.formGroup.reset();
        this.showStoreLocationQuantityInput = false;
        this.goBack()
    }

    getStores(
        page: number = 0,
        size: number = 1000,
        name: string = '',
        isActive: boolean = true
    ) {
          this.storeService.getStores(
            page,
            size,
            name,
            isActive
        ).pipe(
            map((res: StoreResponse) => {
                return res.content
            }),
            catchError((err: any) => {
                return []
            })
        ).subscribe({
            next: (res: any)=>{
                this.storeOptions = res;
                this.storeOptionsNotFiltered = res;
            }
        })
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
        console.log("Search Event", $event)
        this.getProducts(0, 15, $event.trim(), true);
    }

    onItemSelected($event: any, key: string) {
        if ($event == null) {
            if (key == 'productId'){
                this.selectedProduct = $event;
                this.showStoreLocationQuantityInput = false;
                this.formGroup.reset()
                this.formGroup.get('quantity')?.setValue(0)
                this.productNumber$.next('');
                return
            }
            if(key == 'storeId'){
                this.formGroup.get('storeId')?.reset();
                this.formGroup.get('locationId')?.reset();
                this.showLocationInput = !this.showLocationInput
                this.showStoreInput = !this.showStoreInput
                setTimeout(()=>{
                    this.showLocationInput = !this.showLocationInput
                    this.showStoreInput = !this.showStoreInput

                }, 1)
            }
            if(key == 'locationId'){
                this.formGroup.get('locationId')?.reset()
                this.showLocationInput = !this.showLocationInput
                setTimeout(()=>{
                    this.showLocationInput = !this.showLocationInput
                }, 1)
            }
        }else{
        this.formGroup.get(key)?.setValue($event.id)
        }
        if (key == 'storeId') {
            this.selectedStore = $event
        }

        else if (key == 'productId') {
            console.log("eee", $event)
            this.selectedProduct = $event
            this.productNumber$.next($event.productNumber)
            // filter stores
            this.getProductStockInfo(this.selectedProduct.id)
        }
    }

    getProductStockInfo(id: number){
        this.stockService.getProductStockShort(id).subscribe(
            {
                next:(res: StockResponseShort[])=>{
                    this.filterStoreData(res);
                    this.showStoreLocationQuantityInput = true;
                }
            }
        )
    }

    filterStoreData (data : StockResponseShort[]){
        const storeIds = data.map(i=>i.storeId)
        this.storeOptions = this.storeOptionsNotFiltered.filter(i=>!storeIds.includes(i.id))
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

}
