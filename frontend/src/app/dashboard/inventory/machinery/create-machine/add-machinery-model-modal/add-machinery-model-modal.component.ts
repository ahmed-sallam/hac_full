import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {CreateBrand} from "../../../brands/interfaces/CreateBrand";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {TranslatePipe} from "../../../../../pipes/translate.pipe";
import {AsyncPipe} from "@angular/common";
import {
  SelectWithSearchComponent
} from "../../../products/create-product/select-with-search/select-with-search.component";
import {catchError, map, Observable} from "rxjs";
import {BrandEntity, BrandsResponse} from "../../../brands/BrandsResponse";
import {BrandsService} from "../../../brands/brands.service";

@Component({
  selector: 'app-add-machinery-model-modal',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    TranslatePipe,
    AsyncPipe,
    SelectWithSearchComponent
  ],
  templateUrl: './add-machinery-model-modal.component.html',
  styles: ``
})
export class AddMachineryModelModalComponent implements OnInit{
  ngOnInit(): void {
      this.getBrands()
  }

  brandOptions$!: Observable<BrandEntity[]>;
  brandsService: BrandsService = inject(BrandsService);


  @Input() language!: string;
  @Output() hideAddMachineryModelModal: EventEmitter<void> = new EventEmitter<void>();
  @Output() submitForm: EventEmitter<CreateBrand> = new EventEmitter<CreateBrand>();

  formGroup: FormGroup = new FormGroup({
    nameAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
    nameEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
    brand: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
  });


  OnHideAddMachineryModelModal() {
    this.hideAddMachineryModelModal.emit()
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

  searchBrands($event: string) {
    this.getBrands(0, 15,   $event.trim(), true);
  }

  getBrands(
      page: number = 0,
      size: number = 15,
      name: string = '',
      isActive: boolean = true
  ) {
    this.brandOptions$ = this.brandsService.getBrands(
        page,
        size,
        name,
        isActive
    ).pipe(
        map((res: BrandsResponse) => {
          console.log("res1", res)
          return res.content
        }),
        catchError((err) => {
          console.log("err", err)
          return []
        })
    )
  }

  onItemSelected($event: any, key: string) {
    if ($event == null) {
      this.formGroup.get(key)?.setValue('')
    } else {
      this.formGroup.get(key)?.setValue($event.id)
    }
  }




}
