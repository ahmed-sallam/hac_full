import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {TranslatePipe} from "../../../../../pipes/translate.pipe";
import {SelectProductWithSearchComponent} from "../select-product-with-search/select-product-with-search.component";
import {SelectWithSearchComponent} from "../select-with-search/select-with-search.component";
import {AsyncPipe} from "@angular/common";
import {map, Observable} from "rxjs";
import {ProductEntity} from "../../interfaces/ListProductsResponse";
import {ProductsService} from "../../products.service";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

@Component({
    selector: 'app-select-set-items-modal',
    standalone: true,
    imports: [
        TranslatePipe,
        SelectProductWithSearchComponent,
        SelectWithSearchComponent,
        AsyncPipe,
        ReactiveFormsModule,
        FormsModule
    ],
    templateUrl: './select-set-items-modal.component.html',
    styles: ``
})
export class SelectSetItemsModalComponent implements OnInit {
    ngOnInit(): void {
        this.addNewField();
    }

    productsService: ProductsService = inject(ProductsService)

    @Output() hideModal: EventEmitter<any> = new EventEmitter<any>()
    @Output() submitProductSet: EventEmitter<any> = new EventEmitter<any>()
    @Input() language!: string;


    products: any[] = []
    products$!: Observable<ProductEntity[]> | any;

    addNewField() {
        this.products.push({
            product: '',
            quantity: 1,
            productId: 0
        })
    }

    onHideModal() {
        this.hideModal.emit()
    }

    onItemSelected($event: any, index: number) {
        console.log('item selected', $event)
        this.products[index].productId = $event.id
        this.products[index].product = $event.productNumber + ' ' + (this.language == 'ar' ? ($event.mainBrand.nameAr) : ($event.mainBrand.nameEn))
        // this.products[index].quantity = $event.quantity
        this.products$ = null
    }

    searchProduct($event: string) {
        this.products$ = this.productsService.getProducts(0, 20, $event)
            .pipe(
                map((res) => res.content)
            );

    }


    removeItem(index: number) {
        this.products.splice(index, 1)
    }

    // closeModal() {
    //
    // }

    saveProducts() {
        if (this.products.length < 2) {
            alert('Please add at least 2 products') // todo: change to toast
            return
        }
        // get index of products with productId = 0
        const index = this.products.findIndex((p) => p.productId === 0)
        if (index !== -1) {
            alert('Please select a product for all fields') // todo: change to toast
            return
        }
        // get index of products with quantity = 0
        const index2 = this.products.findIndex((p) => p.quantity === 0)
        if (index2 !== -1) {
            alert('Please add a quantity for all fields') // todo: change to toast
            return
        }
        console.log('products', this.products)
        this.submitProductSet.emit(this.products)
    }
}
