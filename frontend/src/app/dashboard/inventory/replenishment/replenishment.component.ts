import {Component, inject, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../state/reducers";
import {catchError, map, Observable, of, switchMap} from "rxjs";
import {LangState} from "../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../state/selectors/lang.selectors";
import {
    FormArray,
    FormBuilder,
    FormControl,
    FormGroup,
    FormsModule,
    ReactiveFormsModule,
    Validators
} from "@angular/forms";
import {
    MainContentComponent
} from "../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../pipes/translate.pipe";
import {AsyncPipe} from "@angular/common";
import {
    SelectProductWithSearchComponent
} from "../products/create-product/select-product-with-search/select-product-with-search.component";
import {ProductsService} from "../products/products.service";
import {ProductEntity} from "../products/interfaces/ListProductsResponse";
import {
    SelectWithSearchComponent
} from "../products/create-product/select-with-search/select-with-search.component";
import {StoreEntity, StoreResponse} from "../stores/interfaces/StoreResponse";
import {StoresService} from "../stores/stores.service";
import {ReplenishmintService} from "./replenishmint.service";
import {ReplenishmentRequest} from "./interfaces/ReplenishmentRequest";
import {ToastService} from "../../../toast/toast.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-replinshment',
    standalone: true,
    imports: [
        MainContentComponent,
        TranslatePipe,
        AsyncPipe,
        ReactiveFormsModule,
        SelectProductWithSearchComponent,
        FormsModule,
        SelectWithSearchComponent
    ],
    templateUrl: './replenishment.component.html',
    styles: ``
})
export class ReplenishmentComponent implements OnInit {

    store: Store<State> = inject(Store<State>)
    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage)
    productsService: ProductsService = inject(ProductsService)
    storeOptions: StoreEntity[] = [];
    router = inject(Router)
    storeService: StoresService = inject(StoresService)
    replenishmentService: ReplenishmintService = inject(ReplenishmintService)
    toastService: ToastService = inject(ToastService)
    fb = inject(FormBuilder);
    // lines: any[] = []
    products$!: Observable<ProductEntity[]> | any;
    formGroup: FormGroup = new FormGroup({
        date: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        store: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        notes: new FormControl(''),
        lines: this.fb.array([this.createLine()])
    });
    // }
    protected readonly FormArray = FormArray;

    get lines(): FormArray {
        return this.formGroup.get('lines') as FormArray;
    }

    createLine(): FormGroup {
        return this.fb.group({
            quantity: new FormControl('', [Validators.required, Validators.min(1)]),
            product: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)]),
            notes: new FormControl(''),
        })
    }

    ngOnInit(): void {
        // this.addNewField();
        this.searchStores('')
        this.searchProduct('')
    }

    searchStores($event: string) {
        this.getStores(0, 15, $event.trim(), true);
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
            }
        })
    }
    onSubmitForm() {
        this.formGroup.markAllAsTouched()
        this.lines.markAllAsTouched()
        console.log("formGroupv", this.formGroup.value)
        console.log("formGroupe", this.formGroup.getError("lines"))
        console.log("aa", this.lines.invalid)

        if (this.formGroup.invalid || this.lines.invalid) {
            console.log("formGroup err", this.formGroup.errors)
        } else {
            console.log("formGroup", this.formGroup.value)
            this.addReplenishment(this.formGroup.value)
        }
    }

    addReplenishment(r: ReplenishmentRequest){
        this.replenishmentService.addReplenishment(r).subscribe({
            next:(id)=>{
                this.toastService.showSuccessToast()
                this.goToMaterialRequest(id);
            },
            error: (r) => {
                this.toastService.showErrorToast()
            }
        });
    }
goToMaterialRequest(id:number){
this.router.navigate(["/dashboard/purchases/material-requests", id]);
}

    addNewField() {
        this.lines.push(this.createLine())
        this.searchProduct('')
    }

    removeItem(index: number) {
        this.lines.removeAt(index)
        this.products$ = null
        // this.lines.splice(index, 1)
    }

    searchProduct($event: string) {
        const productIdInLines = this.lines.value.map((i: any) => i.product);
        console.log(productIdInLines)
        this.products$ = this.productsService.getProducts(0, 20, $event)
            .pipe(
                switchMap((res) => {
                    return of(res.content.filter((product: ProductEntity) => !productIdInLines.includes(product.id)));
                })
            );

    }


    // private goToBrands() {
    //   this.router.navigate(['dashboard/inventory/brands']);

    onItemSelected($event: any, index: number) {

        console.log('item selected', $event)
        if ($event != null) {
            this.lines.at(index).get("product")?.setValue($event.id);
        }else{
            console.log(this.lines.value)
            this.lines.at(index).get('product')?.setValue('')
            console.log(this.lines.value)
        }
        this.products$ = null
    }
    onStoreSelected($event: any) {

        if ($event != null) {
            this.formGroup.get("store")?.setValue($event.id);
        }else{
            this.formGroup.get("store")?.setValue('')
        }
    }

    cancelCreateReplenishment(){
        // this.formGroup.reset();
        // this.formGroup.clearValidators();
        // this.lines.reset()
        // this.lines.clearValidators()
        this.goToStock();
    }

    private goToStock() {
        this.router.navigate(['/dashboard/inventory/stock']);
    }
}

