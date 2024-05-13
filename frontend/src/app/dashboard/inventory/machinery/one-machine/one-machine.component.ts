import {Component} from '@angular/core';
import {LoaderService} from "../../../components/loader/loader.service";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {ActivatedRoute, Router} from "@angular/router";
import {BrandsService} from "../../brands/brands.service";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {MachineryService} from "../machinery.service";
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {BrandEntity} from "../../brands/BrandsResponse";
import {MachineryModelEntity, MachineryTypeEntity} from "../interfaces/MachineryResponse";
import {CreateBrand} from "../../brands/interfaces/CreateBrand";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AddSubBrandModalComponent} from "../../brands/create-brand/add-sub-brand-modal/add-sub-brand-modal.component";
import {AsyncPipe, DatePipe} from "@angular/common";
import {MainContentComponent} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {CreateMachineryModel} from "../interfaces/CreateMachineryModel";
import {
    AddMachineryModelModalComponent
} from "../create-machine/add-machinery-model-modal/add-machinery-model-modal.component";

@Component({
    selector: 'app-one-machine',
    standalone: true,
    imports: [
        AddSubBrandModalComponent,
        AsyncPipe,
        DatePipe,
        MainContentComponent,
        ReactiveFormsModule,
        TranslatePipe,
        AddMachineryModelModalComponent
    ],
    templateUrl: './one-machine.component.html',
    styles: ``
})
export class OneMachineComponent {
    private selectedMachinerModel!: MachineryModelEntity|any;

    constructor(
        private loaderService: LoaderService,
        private store: Store<State>,
        private activeRouter: ActivatedRoute,
        private machineryService: MachineryService,
        private router: Router,
    ) {
        this.loader$ = this.loaderService.isLoading;
        this.selectLanguage$ = this.store.select(selectLanguage);
    }

    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage)
    machinery$!: Observable<MachineryTypeEntity>;
    loader$!: Observable<boolean>;
    machineryId !: number;
    machineryModelId!: number;

    showAddMachineryModelModal: boolean = false;
    showEditMachineryTypeModal: boolean = false;
    showEditMachineryModelModal: boolean = false;


    tableColumns: string[] = [
        '#',
        'Name',
        'Brand',
        'Status',
        'Last updated',
        'Actions',
    ];

    ngOnInit(): void {
        this.initPageParams();
    }

    private initPageParams() {
        this.loaderService.show()
        this.activeRouter.params.subscribe(params => {
            this.machineryId = params['id'];
            this.machineryId && this.getData()
        })
    }

    getData() {
        this.machinery$ = this.machineryService.getMachineryType(this.machineryId);
    }

    showAddMachineryModelModalF() {
        this.showAddMachineryModelModal = true;
    }

    hideAddMachineryModelModal() {
        this.showAddMachineryModelModal = false;
    }

    onSubmitForm($event: CreateMachineryModel) {
        // todo: set default active on backend to true
        $event.machineryType = this.machineryId
        $event.isActive = true
        this.machineryService.addMachineryModel(this.machineryId, $event).subscribe({
            next: (res) => {
                console.log("res", res)
                this.getData()
                this.hideAddMachineryModelModal()
            },
            error: (err) => {
                console.log("err", err)
            }
        })
    }

    editMachineryTypeForm: FormGroup = new FormGroup({
        nameAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        nameEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        isActive: new FormControl(true,),
    });

    editMachineryModelForm: FormGroup = new FormGroup({
        nameAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        nameEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        // code: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        // ]), // todo active this for brand
        isActive: new FormControl(true,),
    });

    changeEditMachineryTypeVisibility(b: boolean) {
        this.showEditMachineryTypeModal = b;
    }

    changeEditMachineryModelVisibility(b: boolean) {
        this.showEditMachineryModelModal = b;
        if(!b){
            this.machineryModelId = 0
            this.selectedMachinerModel = null;
        }
        // b ? '' : this.machineryModelId = 0
    }

    onSubmitEditMachineryTypeForm() {
        if (this.editMachineryTypeForm.invalid) {
            return;
        }
        const {
            nameAr,
            nameEn,
            isActive
        } = this.editMachineryTypeForm.value

        this.machineryService.updateMachineryType(this.machineryId,
            {
                nameAr,
                nameEn,
                isActive,
            }).subscribe({
            next: (res) => {
                console.log("res", res)
                this.editMachineryTypeForm.reset()
                this.getData()
                this.changeEditMachineryTypeVisibility(false)
            },
            error: (err) => {
                console.log("err", err)
            }
        })
    }

    onSubmitEditMachineryModelForm() {
        if (this.editMachineryModelForm.invalid) {
            return;
        }
        const {
            nameAr,
            nameEn,
            // code,
            isActive
        } = this.editMachineryModelForm.value
        this.machineryService.updateMachineryModel(this.machineryId, this.machineryModelId, {
            nameAr,
            nameEn,
            machineryType: this.selectedMachinerModel.machineryType,
            brand:this.selectedMachinerModel.brand.id,
            // code,
            isActive
        }).subscribe({
            next: (res) => {
                console.log("res", res)
                this.editMachineryModelForm.reset()
                this.getData()
                this.changeEditMachineryModelVisibility(false)
            },
            error: (err) => {
                console.log("err", err)
            }
        })
    }

    onshowEditMachineryModelModal($event: MachineryModelEntity) {
        this.machineryModelId = $event.id
        this.selectedMachinerModel = $event
        this.editMachineryModelForm.setValue({
            nameAr: $event.nameAr,
            nameEn: $event.nameEn,
            // code: $event.code, // todo active this fr brand
            isActive: $event.isActive
        })
        this.changeEditMachineryModelVisibility(true)
    }

    onshowEditMachineryTypeModal() {
        this.machinery$.subscribe((data) => {
            this.editMachineryTypeForm.controls['nameEn'].setValue(data.nameEn)
            this.editMachineryTypeForm.controls['nameAr'].setValue(data.nameAr)
            this.editMachineryTypeForm.controls['isActive'].setValue(data.isActive)
        })
        this.changeEditMachineryTypeVisibility(true)
    }


}
