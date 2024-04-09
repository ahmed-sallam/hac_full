import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {TranslatePipe} from "../../../../../pipes/translate.pipe";
import {CreateLocation} from "../../interfaces/CreateLocation";


@Component({
  selector: 'app-add-store-location-modal',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    TranslatePipe
  ],
  templateUrl: './add-store-location-modal.component.html',
  styles: ``
})
export class AddStoreLocationModalComponent {
  @Input() language!: string;
  @Output() hideAddLocationModal: EventEmitter<void> = new EventEmitter<void>();
  @Output() submitForm: EventEmitter<CreateLocation> = new EventEmitter<CreateLocation>();

  formGroup: FormGroup = new FormGroup({
    nameAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
    nameEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
  });


  OnHideAddLocationModal() {
    this.hideAddLocationModal.emit()
  }

  onSubmitForm() {
    this.formGroup.markAllAsTouched()
    if (this.formGroup.invalid) {
      console.log("formGroup err", this.formGroup.errors)
    }else{
      console.log("formGroup", this.formGroup.value)
      this.submitForm.emit(this.formGroup.value)
    }
  }
}
