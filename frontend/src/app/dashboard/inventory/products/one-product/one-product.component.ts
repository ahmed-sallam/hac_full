import {Component, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {BehaviorSubject, Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {MainContentComponent} from "../../../components/main-content/main-content.component";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {AsyncPipe, DatePipe, NgClass} from "@angular/common";
import {ProductsService} from "../products.service";
import {ProductEntity} from "../interfaces/ListProductsResponse";
import {EmptyDivComponent} from "../../../components/empty-div/empty-div.component";
import {FilesService} from "../../../../files.service";
import {LoaderService} from "../../../components/loader/loader.service";
import {LoaderComponent} from "../../../components/loader/loader.component";
import {OneProductTableComponent} from "./one-product-table/one-product-table.component";
import {StockService} from "../../stock/stock.service";
import {StockResponseShort} from "../interfaces/StockResponseShort";

@Component({
    selector: 'app-one-product',
    standalone: true,
    imports: [
        MainContentComponent,
        RouterLink,
        TranslatePipe,
        AsyncPipe,
        DatePipe,
        NgClass,
        EmptyDivComponent,
        LoaderComponent,
        OneProductTableComponent
    ],
    templateUrl: './one-product.component.html',
    styles: ``
})
export class OneProductComponent implements OnInit {
    constructor(
        private loaderService: LoaderService,
        private filesService: FilesService,
        private store: Store<State>,
        private activeRouter: ActivatedRoute,
        private productsService: ProductsService,
        private stockService: StockService,
        private router: Router,
    ) {
        this.loader$ = this.loaderService.isLoading;
        this.selectLanguage$ = this.store.select(selectLanguage);
    }


    selectLanguage$!: Observable<LangState>;
    product!: ProductEntity;
    productId!: number;
    tabsHeader: string[] = [
        'Same Items',
        'Alternative',
        'Set Items',
        'Stock'
    ]
    tableColumns: string[] = [
        'Number',
        'Brand',
        'Sub Brand',
        'Country',
        'Is Original',
        'Unit',
    ];

    selectedTab: number = 0;
    // alternatives: ProductEntity[] = [];
    // sameItems: ProductEntity[] = [];
    stock : StockResponseShort[]  | any = [];
    selectedImage!: string;
    loader$!: Observable<boolean>;
    loadImageComplete: number = 0;
    imageLoading$ = new BehaviorSubject<boolean>(false);

    ngOnInit(): void {
        this.initPageParams();

    }

    private initPageParams() {
        this.loaderService.show()
        this.activeRouter.params.subscribe(params => {
            this.productId = params['id'];
            this.productId && this.getData()
            this.productId &&  this.getProductStock()
        })
    }

    getData() {
        this.loaderService.show()
        // ..set image loading
        this.loadImageComplete = 0
        this.imageLoading$.next(true)

        this.productsService.getOneProduct(this.productId)
            .subscribe({
                next: (response: ProductEntity) => {
                    console.log(response)
                    this.product = response;

                    this.filesService.getFile(response.image).subscribe({
                        next: (res) => {
                            this.product.image = URL.createObjectURL(res)
                            this.selectedImage = this.product.image
                            this.loadImageComplete++
                            console.log('1', this.loadImageComplete)

                            if (this.loadImageComplete == 2) {
                                this.imageLoading$.next(false)
                            }
                        }
                    })
                    this.filesService.getFile(response.partImage).subscribe({
                        next: (res) => {
                            this.product.partImage = URL.createObjectURL(res)
                            this.loadImageComplete++
                            console.log('2', this.loadImageComplete)
                            if (this.loadImageComplete == 2) {
                                this.imageLoading$.next(false)
                            }
                        }
                    })
                    this.loaderService.hide()

                }
            });
    }

    // getSameItems() {
    //     this.productsService.getProducts(0, 50, this.product.productNumber, true)
    //         .subscribe(
    //             {
    //                 next: (response: ListProductsResponse) => {
    //                     this.sameItems = response.content.filter((item: ProductEntity) => item.id != this.productId);
    //                 }
    //             }
    //         );
    // }

    getProductStock(){
        this.stockService.getProductStockShort(this.productId).subscribe({
            next:(res: StockResponseShort[])=>{
                let grouped = res.reduce((acc: {} | any, item: StockResponseShort) => {
                    (acc[item.storeNameEn] = acc[item.storeNameEn] || []).push(item);
                    return acc;
                }, {});

                for (let store in grouped) {
                    grouped[store].sort((a: any, b: any) => a.locationNameEn.localeCompare(b.locationNameEn));
                }
                let sortedAndNested = Object.values(grouped).map((group:any) =>
                    group.sort((a:any, b:any) => a.locationNameEn.localeCompare(b.locationNameEn))
                );

                console.log(grouped);
                console.log(sortedAndNested);
                this.stock=sortedAndNested;

            }
        })
    }

    onTabSelect($index: number) {
        this.selectedTab = $index;
    }

    onImageSelected(image: string) {
        this.selectedImage = image;
    }

    goToItem(id: number) {
        this.router.navigate([`/dashboard/inventory/products/${id}`])
    }

    getStoreTotalStock(item: any) {
        return item.reduce((a:number, i:StockResponseShort)=>{
            return i.quantity + a
        }, 0);
    }
}
