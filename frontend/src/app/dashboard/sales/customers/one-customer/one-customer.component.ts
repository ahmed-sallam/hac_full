import { AsyncPipe } from "@angular/common";
import { Component, OnInit, inject } from "@angular/core";
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { TranslatePipe } from "../../../../pipes/translate.pipe";
import { State } from "../../../../state/reducers";
import { LangState } from "../../../../state/reducers/lang.reducer";
import { selectLanguage } from "../../../../state/selectors/lang.selectors";
import { LoaderService } from "../../../components/loader/loader.service";
import { MainContentComponent } from "../../../components/main-content/main-content.component";
import { CustomersService } from "../customers.service";
import { CustomerEntity } from "../interfaces/CustomersResponse";

@Component({
  selector: "app-one-customer",
  standalone: true,
  imports: [
    AsyncPipe,
    TranslatePipe,
    MainContentComponent,
    FormsModule,
    ReactiveFormsModule,
  ],
  templateUrl: "./one-customer.component.html",
  styles: ``,
})
export class OneCustomerComponent implements OnInit {
  store: Store<State> = inject(Store<State>);
  selectLanguage$: Observable<LangState> = this.store.select(selectLanguage);
  activeRouter: ActivatedRoute = inject(ActivatedRoute);
  router: Router = inject(Router);
  customersService: CustomersService = inject(CustomersService);
  loader$!: Observable<boolean>;
  loaderService: LoaderService = inject(LoaderService);
  customer$!: Observable<CustomerEntity>;
  showEditModal: boolean = false;

  customerId!: number;

  formGroup: FormGroup = new FormGroup({
    nameAr: new FormControl("", [
      Validators.required,
      Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/),
    ]),
    nameEn: new FormControl("", [
      Validators.required,
      Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/),
    ]),
    email: new FormControl("", [Validators.email]),
    phone: new FormControl(""),
    address: new FormControl(""),
    isActive: new FormControl(),
  });

  ngOnInit(): void {
    this.loader$ = this.loaderService.isLoading;
    this.selectLanguage$ = this.store.select(selectLanguage);
    this.initPageParams();
  }

  private initPageParams() {
    this.loaderService.show();
    this.activeRouter.params.subscribe((params) => {
      this.customerId = params["id"];
      this.customerId && this.getData();
    });
  }

  getData() {
    this.customer$ = this.customersService.getCustomerById(this.customerId);
  }

  changeEditModalStatus(status: boolean) {
    if (status) {
      this.onShowCustomer();
    }
    this.showEditModal = status;
  }

  updateCustoemr() {
    this.formGroup.markAllAsTouched();
    if (this.formGroup.invalid) {
    } else {
      this.customersService
        .updateCustomer(this.customerId, this.formGroup.value)
        .subscribe({
          next: (res) => {
            this.formGroup.reset();
            this.getData();
            this.changeEditModalStatus(false);
          },

          error: (error) => {},
        });
    }
  }

  onShowCustomer() {
    this.customer$.subscribe((customer) => {
      this.formGroup.controls["nameEn"].setValue(customer.nameEn);
      this.formGroup.controls["nameAr"].setValue(customer.nameAr);
      this.formGroup.controls["email"].setValue(customer.email);
      this.formGroup.controls["address"].setValue(customer.address);
      this.formGroup.controls["phone"].setValue(customer.phone);
      this.formGroup.controls["isActive"].setValue(customer.isActive);
    });
  }
}
