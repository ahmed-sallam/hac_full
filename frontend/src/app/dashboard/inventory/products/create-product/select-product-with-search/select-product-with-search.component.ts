import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import {ProductEntity} from "../../interfaces/ListProductsResponse";
import {TranslatePipe} from "../../../../../pipes/translate.pipe";

@Component({
  selector: 'app-select-product-with-search',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    TranslatePipe
  ],
  templateUrl: './select-product-with-search.component.html',
  styles: ``
})
export class SelectProductWithSearchComponent {


  showOptions: boolean = false;
  @Output() searchInput: EventEmitter<string> = new EventEmitter<string>()
  @Output() selectedItem: EventEmitter<any> = new EventEmitter<any>()
  @Input() searchOptions: null|any[]|ProductEntity[]=[];
  @Input() language: string = 'en';

  @Input() inputValue: string=''




  showOptionsd(s:boolean) {
    this.showOptions = s;
  }

  search($event: KeyboardEvent) {
    this.searchInput.emit(($event.target as HTMLInputElement).value)
  }
  onItemSeclected(item: any) {
    // this.inputValue = item.productNumber
    this.inputValue = item.productNumber +' '+( this.language  == 'ar' ? (item.mainBrand.nameAr ) : (item.mainBrand.nameEn ))
    this.selectedItem.emit(item)
  }

  reset() {
    this.inputValue='';
    this.selectedItem.emit(null)
  }

  protected readonly setTimeout = setTimeout;

  fOut() {
    setTimeout(() => {
      this.showOptions = false
    }, 100)
  }
}
