import {Component, inject} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {SuppliersService} from "../suppliers.service";
import {Router} from "@angular/router";
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from "@angular/forms";
import {CreateSupplier} from "../CreateSupplier";
import {AsyncPipe} from "@angular/common";
import {
  MainContentComponent
} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";

@Component({
  selector: 'app-create-supplier',
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    TranslatePipe,
    ReactiveFormsModule
  ],
  templateUrl: './create-supplier.component.html',
  styles: ``
})
export class CreateSupplierComponent {
  store: Store<State> = inject(Store<State>)
  selectLanguage$: Observable<LangState> = this.store.select(selectLanguage)
  suppliersService = inject(SuppliersService);

  router: Router = inject(Router)

  formGroup: FormGroup = new FormGroup({
    nameAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
    nameEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
    email: new FormControl('', [Validators.email]),
    phone: new FormControl(''),
    address: new FormControl('' ),
  });

  onSubmitForm() {
    this.formGroup.markAllAsTouched()
    if (this.formGroup.invalid) {
      }else{
      this.addSupplier(this.formGroup.value)

      }
  }

  cancelCreate() {
    this.formGroup.reset();
    this.gotoSuppliers()
  }

  private addSupplier(supplier:CreateSupplier) {
    supplier.isActive = true
    this.suppliersService.addSupplier(supplier).subscribe({
      next: (res) => {
        this.gotoSuppliers()

        },
      error: (err) => {
        }
    })
  }

  private gotoSuppliers() {
    this.router.navigate(['/dashboard/purchases/suppliers']);
  }
}
