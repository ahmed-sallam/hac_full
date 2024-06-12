import {Component, EventEmitter, Input, Output} from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators
} from "@angular/forms";
import {CreateBrand} from "../../interfaces/CreateBrand";
import {TranslatePipe} from "../../../../../pipes/translate.pipe";

@Component({
  selector: 'app-add-sub-brand-modal',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    TranslatePipe
  ],
  templateUrl: './add-sub-brand-modal.component.html',
  styles: ``
})
export class AddSubBrandModalComponent {
  @Input() language!: string;
  @Output() hideAddLocationModal: EventEmitter<void> = new EventEmitter<void>();
  @Output() submitForm: EventEmitter<CreateBrand> = new EventEmitter<CreateBrand>();

  formGroup: FormGroup = new FormGroup({
    nameAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
    nameEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
    code: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
  });


  OnHideAddLocationModal() {
    this.hideAddLocationModal.emit()
  }

  onSubmitForm() {
    this.formGroup.markAllAsTouched()
    if (!this.formGroup.invalid) {
      this.submitForm.emit(this.formGroup.value)
      }
  }
}
