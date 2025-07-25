import {Component, inject} from '@angular/core';
import {AsyncPipe} from "@angular/common";
import {
  MainContentComponent
} from "../../../components/main-content/main-content.component";
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from "@angular/forms";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {Router} from "@angular/router";
import {MachineryService} from "../machinery.service";
import {CreateBrand} from "../../brands/interfaces/CreateBrand";

@Component({
  selector: 'app-create-machine',
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    ReactiveFormsModule,
    TranslatePipe
  ],
  templateUrl: './create-machine.component.html',
  styles: ``
})
export class CreateMachineComponent {

  store: Store<State> = inject(Store<State>)
  selectLanguage$: Observable<LangState> = this.store.select(selectLanguage)
  machineryService: MachineryService = inject(MachineryService)
  router: Router = inject(Router)
  formGroup: FormGroup = new FormGroup({
    nameAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
    nameEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
  });

  onSubmitForm() {
    this.formGroup.markAllAsTouched()
    if (!this.formGroup.invalid) {
      this.addMachinery(this.formGroup.value)
      }
  }

  cancelCreateMachineryType() {
    this.formGroup.reset();
    this.goToMachinery()
  }

  private addMachinery(part: CreateBrand) {
    part.isActive = true
    this.machineryService.addMachineryType(part).subscribe({
      next: (res) => {
        this.goToMachinery()
        },
      error: (err) => {
        }
    })
  }

  private goToMachinery() {
    this.router.navigate(['dashboard/inventory/machinery']);
  }

}
