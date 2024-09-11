import { Component, inject } from '@angular/core';
import { FormGroup, FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { LangState } from '../../../../state/reducers/lang.reducer';
import { selectLanguage } from '../../../../state/selectors/lang.selectors';
import { CustomersService } from '../customers.service';
import { State } from '../../../../state/reducers';
import { CreateCustomer } from '../interfaces/CreateCustomer';
import { AsyncPipe } from '@angular/common';
import { TranslatePipe } from '../../../../pipes/translate.pipe';
import { MainContentComponent } from '../../../components/main-content/main-content.component';

@Component({
  selector: 'app-create-customer',
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    TranslatePipe,
    ReactiveFormsModule
  ],
  templateUrl: './create-customer.component.html',
  styles: ``
})
export class CreateCustomerComponent {

  store: Store<State> = inject(Store<State>)
  selectLanguage$: Observable<LangState> = this.store.select(selectLanguage)
  customersService = inject(CustomersService);
  router: Router = inject(Router)

  formGroup: FormGroup = new FormGroup({
    nameAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
    nameEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
    email: new FormControl('', [Validators.email]),
    phone: new FormControl(''),
    address: new FormControl('')
  });

  onSubmitForm() {
    this.formGroup.markAllAsTouched()
    if (this.formGroup.invalid) {
      }else{
      this.addCustomer(this.formGroup.value)

      }
  } 

  cancelCreate() {
    this.formGroup.reset();
    this.gotoCustomers()
  }

  private addCustomer(customer: CreateCustomer) {
    customer.isActive = true
    this.customersService.addCustomer(customer).subscribe({
      next: (res) => {
        this.gotoCustomers()
      }
    })
  } 

  private gotoCustomers() {
    this.router.navigate(['/dashboard/sales/customers']);
  }
}
