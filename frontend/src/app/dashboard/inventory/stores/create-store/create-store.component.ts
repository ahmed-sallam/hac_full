import {Component, inject} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {
    FormControl,
    FormGroup,
    ReactiveFormsModule,
    Validators
} from "@angular/forms";
import {AsyncPipe} from "@angular/common";
import {
    MainContentComponent
} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {StoresService} from "../stores.service";
import {ToastService} from "../../../../toast/toast.service";

@Component({
    selector: 'app-create-store',
    standalone: true,
    imports: [AsyncPipe,
        MainContentComponent,
        TranslatePipe,
        ReactiveFormsModule],
    templateUrl: './create-store.component.html',
    styles: ``
})
export class CreateStoreComponent {
    store = inject(Store<State>)
    storeService = inject(StoresService)
    toastService = inject(ToastService)
    selectLanguage$ = this.store.select(selectLanguage)

    formGroup: FormGroup = new FormGroup({
        nameAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        nameEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        cityAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        cityEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        isActive: new FormControl(true,),
    });

    onSubmitForm() {
        if (this.formGroup.invalid) {
            return;
        }
        this.storeService.addStore(this.formGroup.value).subscribe({
            next: () => {
                this.formGroup.reset()
                this.toastService.showSuccessToast()
                this.goBack()
            },
            error: (r) => {
                this.toastService.showErrorToast()
                }
        })
    }

    // generate go back to the previous page
    goBack() {
        window.history.back();
    }

    cancelCreateStore() {
        this.formGroup.reset();
        this.goBack()
    }
}