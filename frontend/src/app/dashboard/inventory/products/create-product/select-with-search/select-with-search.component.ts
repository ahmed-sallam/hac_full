import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-select-with-search',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './select-with-search.component.html',
  styles: ``
})
export class SelectWithSearchComponent {
  showOptions: boolean = false;
  @Output() searchInput: EventEmitter<string> = new EventEmitter<string>()
  @Output() selectedItem: EventEmitter<any> = new EventEmitter<any>()
  @Input() searchOptions: null|any[]=[];
  @Input() language: string = 'en';
  @Input() product: boolean=false;

  @Input() inputValue: string|any=''
  showOptionsd(s:boolean) {
    this.showOptions = s;
  }

  search($event: KeyboardEvent) {
    this.searchInput.emit(($event.target as HTMLInputElement).value)
  }
  onItemSeclected(item: any) {
    this.inputValue =this.product ? item.productNumber : this.language  == 'ar' ? item.nameAr : item.nameEn
    this.selectedItem.emit(item)
  }

  reset() {
    this.inputValue='';
    this.selectedItem.emit(null)
  }



  fOut() {
    setTimeout(() => {
      this.showOptions = false
    }, 100)
  }
}
