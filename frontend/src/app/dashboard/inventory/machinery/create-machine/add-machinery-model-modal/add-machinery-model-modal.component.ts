import { BrandEntity, BrandsResponse } from "../../../brands/BrandsResponse";
import {
  Component,
  EventEmitter,
  Input,
  OnInit,
  Output,
  inject,
} from "@angular/core";
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from "@angular/forms";
import { Observable, catchError, map } from "rxjs";

import { AsyncPipe } from "@angular/common";
import { BrandsService } from "../../../brands/brands.service";
import { CreateBrand } from "../../../brands/interfaces/CreateBrand";
import { SelectWithSearchComponent } from "../../../products/create-product/select-with-search/select-with-search.component";
import { TranslatePipe } from "../../../../../pipes/translate.pipe";

@Component({
  selector: "app-add-machinery-model-modal",
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    TranslatePipe,
    AsyncPipe,
    SelectWithSearchComponent,
  ],
  templateUrl: "./add-machinery-model-modal.component.html",
  styles: ``,
})
export class AddMachineryModelModalComponent implements OnInit {
  brandOptions$!: Observable<BrandEntity[]>;
  brandsService: BrandsService = inject(BrandsService);
  @Input() language!: string;
  @Output() hideAddMachineryModelModal: EventEmitter<void> =
    new EventEmitter<void>();
  @Output() submitForm: EventEmitter<CreateBrand> =
    new EventEmitter<CreateBrand>();
  formGroup: FormGroup = new FormGroup({
    nameAr: new FormControl("", [
      Validators.required,
      Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/),
    ]),
    nameEn: new FormControl("", [
      Validators.required,
      Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/),
    ]),
    brand: new FormControl("", [
      Validators.required,
      Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/),
    ]),
  });

  ngOnInit(): void {
    this.getBrands();
  }

  OnHideAddMachineryModelModal() {
    this.hideAddMachineryModelModal.emit();
  }

  onSubmitForm() {
    this.formGroup.markAllAsTouched();
    if (!this.formGroup.invalid) {
      this.submitForm.emit(this.formGroup.value);
    }
  }
  searchBrands($event: string) {
    this.getBrands(0, 80, $event.trim(), true);
  }

  getBrands(
    page: number = 0,
    size: number = 15,
    name: string = "",
    isActive: boolean = true
  ) {
    this.brandOptions$ = this.brandsService
      .getBrands(page, size, name, isActive)
      .pipe(
        map((res: BrandsResponse) => {
          return res.content;
        }),
        catchError((err) => {
          return [];
        })
      );
  }

  onItemSelected($event: any, key: string) {
    if ($event == null) {
      this.formGroup.get(key)?.setValue("");
    } else {
      this.formGroup.get(key)?.setValue($event.id);
    }
  }
}
