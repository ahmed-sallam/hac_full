import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DatePipe} from "@angular/common";
import {TranslatePipe} from "../../../../pipes/translate.pipe";

@Component({
  selector: 'app-purchase-quotation-modal',
  standalone: true,
  imports: [
    DatePipe,
    TranslatePipe
  ],
  templateUrl: './purchase-quotation-modal.component.html',
  styles: ``
})
export class PurchaseQuotationModalComponent {
  @Input() language: string = 'en';
  @Input() supplier!: any;
  @Output() closeModal = new EventEmitter<void>();
  @Input() quotation!: any;
  onClose(){
    this.closeModal.emit();
  }

}
