import {Component, EventEmitter, Input, Output} from '@angular/core';
import {TranslatePipe} from "../../../../../pipes/translate.pipe";
import {ProductEntity} from "../../interfaces/ListProductsResponse";

@Component({
  selector: 'app-one-product-table',
  standalone: true,
  imports: [
    TranslatePipe
  ],
  templateUrl: './one-product-table.component.html',
  styles: ``
})
export class OneProductTableComponent {
  @Input() tableColumns!: string[];
  @Input() language!: string;
  @Input() products!: ProductEntity[];
  @Output() goToProduct: EventEmitter<number> = new EventEmitter<number>();

  goToItem(id: number) {
    this.goToProduct.emit(id);
  }
}
