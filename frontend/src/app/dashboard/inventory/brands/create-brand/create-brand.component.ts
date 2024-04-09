import {Component, inject} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {AsyncPipe, DatePipe} from "@angular/common";
import {MainContentComponent} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {SearchInputComponent} from "../../../components/search-input/search-input.component";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {BrandsService} from "../brands.service";
import {CreateBrand} from "../interfaces/CreateBrand";

@Component({
  selector: 'app-create-brand',
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    TranslatePipe,
    RouterLink,
    SearchInputComponent,
    DatePipe,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './create-brand.component.html',
  styles: ``
})
export class CreateBrandComponent {

  store: Store<State> = inject(Store<State>)
  selectLanguage$: Observable<LangState> = this.store.select(selectLanguage)
  brandsService: BrandsService = inject(BrandsService)
  router: Router = inject(Router)
  formGroup: FormGroup = new FormGroup({
    nameAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
    nameEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
  });

  onSubmitForm() {
    this.formGroup.markAllAsTouched()
    if (this.formGroup.invalid) {
      console.log("formGroup err", this.formGroup.errors)
    }else{
      console.log("formGroup", this.formGroup.value)
      this.addBrand(this.formGroup.value)
    }
  }

  private addBrand(part: CreateBrand) {
    part.isActive = true
    this.brandsService.addBrand(part).subscribe({
      next: (res) => {
        console.log("res", res)
        this.goToBrands()
      },
      error: (err) => {
        console.log("err", err)
      }
    })
  }

  cancelCreateBrand() {
    this.formGroup.reset();
    this.goToBrands()
  }

  private goToBrands() {
    this.router.navigate(['dashboard/inventory/brands']);
  }


}
