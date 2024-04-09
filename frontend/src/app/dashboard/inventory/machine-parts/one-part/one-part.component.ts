import {Component, OnInit} from '@angular/core';
import {AsyncPipe, DatePipe} from "@angular/common";
import {MainContentComponent} from "../../../components/main-content/main-content.component";
import {SearchInputComponent} from "../../../components/search-input/search-input.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {MachinePartsService} from "../machine-parts.service";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {MachinePartEntity} from "../interfaces/MachinePartResponse";
import {LoaderService} from "../../../components/loader/loader.service";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";

@Component({
  selector: 'app-one-part',
  standalone: true,
    imports: [
        AsyncPipe,
        DatePipe,
        MainContentComponent,
        SearchInputComponent,
        TranslatePipe,
        RouterLink,
        FormsModule,
        ReactiveFormsModule
    ],
  templateUrl: './one-part.component.html',
  styles: ``
})
export class OnePartComponent implements  OnInit{
    constructor(
        private loaderService: LoaderService,
        private store: Store<State>,
        private activeRouter: ActivatedRoute,
        private machinePartService: MachinePartsService,
        private router: Router,
    ) {
        this.loader$ = this.loaderService.isLoading;
        this.selectLanguage$ = this.store.select(selectLanguage);
    }


    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage)
    machinePart$!: Observable<MachinePartEntity>;
    loader$!: Observable<boolean>;
    partId!: number;
    showEditMachinePartModal: boolean = false;

    ngOnInit(): void {
        this.initPageParams();
    }
    private initPageParams() {
        this.loaderService.show()
        this.activeRouter.params.subscribe(params => {
            this.partId = params['id'];
            this.partId && this.getData()
        })
    }
    getData(){
        this.machinePart$ = this.machinePartService.getMachinePart(this.partId);
    }

    changeEditModalVisibility(b: boolean) {
        this.showEditMachinePartModal = b;
    }

    // For edit modal

    formGroup: FormGroup = new FormGroup({
        nameAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        nameEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        isActive: new FormControl(true,),
    });


    onSubmitForm() {
        console.log("formGroup", this.formGroup.value)
        if (this.formGroup.invalid) {
            return;
        }
        const {
            nameAr,
            nameEn,
            isActive
        } = this.formGroup.value

        this.machinePartService.updateMachinePart(this.partId, {
            nameAr,
            nameEn,
            isActive
        }).subscribe({
            next: () => {
                this.formGroup.reset()
                this.getData()
                this.changeEditModalVisibility(false)
            },
            error: (r) => {
                console.log("error", r)

            }
        })
    }

    showEditModalAndSetData() {
        this.machinePart$.subscribe((data) => {
            this.formGroup.controls['nameEn'].setValue(data.nameEn)
            this.formGroup.controls['nameAr'].setValue(data.nameAr)
            this.formGroup.controls['isActive'].setValue(data.isActive)
        })
        this.changeEditModalVisibility(true)
    }
}
