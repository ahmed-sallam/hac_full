import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-quotations-modal',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    TranslatePipe,
    DatePipe
  ],
  templateUrl: './quotations-modal.component.html',
  styles: ``
})
export class QuotationsModalComponent {
  @Input() language!: string;


  tableColumns = [
      'Id',
      'Date',
      'Product',
      'Net Price',
      'Sar Price',
      'Is Local',
      'Payment Terms',
        'Delivery'
  ]
    @Input() quotations!: any;
    @Input() supplier!: any;
    @Output() closeModal = new EventEmitter<void>();
    @Output() selectQuotation = new EventEmitter<any>();

   onClose(){
       this.closeModal.emit();
   }

    OnSelectItem(item: any) {
        this.selectQuotation.emit(item);
    }

    selectItemClose(item: any) {
       this.OnSelectItem(item);
        this.onClose();
    }
}
