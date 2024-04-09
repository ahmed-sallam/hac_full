import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {ProductsService} from "../../products.service";
import {map, Observable} from "rxjs";
import {ProductEntity} from "../../interfaces/ListProductsResponse";
import {AsyncPipe} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {SelectProductWithSearchComponent} from "../select-product-with-search/select-product-with-search.component";
import {TranslatePipe} from "../../../../../pipes/translate.pipe";

@Component({
  selector: 'app-select-alternative-modal',
  standalone: true,
  imports: [
    AsyncPipe,
    FormsModule,
    SelectProductWithSearchComponent,
    TranslatePipe
  ],
  templateUrl: './select-alternative-modal.component.html',
  styles: ``
})
export class SelectAlternativeModalComponent implements OnInit {
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
      productNumber:''
    })
  }

  onHideModal() {
    this.hideModal.emit()
  }

  onItemSelected($event: any, index: number) {
    console.log('item selected', $event)
    this.products[index].productNumber = $event.productNumber
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
    if (this.products.length < 1) {
      alert('Please add at least 1 products') // todo: change to toast
      return
    }
    // get index of products with productId = 0
    const index = this.products.findIndex((p) => p.productNumber.length < 1)
    if (index !== -1) {
      alert('Please select a product for all fields') // todo: change to toast
      return
    }

    console.log('products', this.products)
    this.submitProductSet.emit(this.products)
  }
}
