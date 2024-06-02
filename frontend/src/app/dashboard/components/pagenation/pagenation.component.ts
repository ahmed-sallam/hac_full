import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgClass} from "@angular/common";
import {Pageable} from "../../inventory/stores/interfaces/StoreResponse";
import {LangState} from "../../../state/reducers/lang.reducer";

@Component({
  selector: 'app-pagenation',
  standalone: true,
  imports: [
    NgClass
  ],
  templateUrl: './pagenation.component.html',
  styles: ``
})
export class PagenationComponent {

  @Output() onPageChanged: EventEmitter<any> = new EventEmitter<any>()
  @Input() language!: LangState;
  @Input() response!: any;
  @Input() currentPage!: number;
  @Input() pageArray!: any[];
  @Input() pageable!: Pageable;


  onPageChangedF(page:number){
    this.onPageChanged.emit(page)
  }
}
