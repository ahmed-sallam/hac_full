import {Component, inject} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {AsyncPipe} from "@angular/common";
import {
    MainContentComponent
} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {
    FormControl,
    FormGroup,
    ReactiveFormsModule,
    Validators
} from "@angular/forms";
import {Router} from "@angular/router";
import {CreateMachinePart} from "../interfaces/CreateMachinePart";
import {MachinePartsService} from "../machine-parts.service";
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";

@Component({
    selector: 'app-create-parts',
    standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        TranslatePipe,
        ReactiveFormsModule
    ],
    templateUrl: './create-parts.component.html',
    styles: ``
})
export class CreatePartsComponent {
    store: Store<State> = inject(Store<State>)
    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage)
    machinePartService: MachinePartsService = inject(MachinePartsService)
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
            this.addMachinePart(this.formGroup.value)
            }
    }

    cancelCreatePart() {
        this.formGroup.reset();
        this.goToMachineParts()
    }

    private addMachinePart(part: CreateMachinePart) {
    this.machinePartService.addMachinePart(part).subscribe({
        next: (res) => {
            this.goToMachineParts()
            },
        error: (err) => {
            }
    })
    }

    private goToMachineParts() {
        this.router.navigate(['dashboard/inventory/machine-parts']);
    }


}
