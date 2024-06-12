import {Component} from '@angular/core';
import {LoaderService} from "../../../components/loader/loader.service";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {StoresService} from "../stores.service";
import {
    MainContentComponent
} from "../../../components/main-content/main-content.component";
import {AsyncPipe, DatePipe} from "@angular/common";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {StoreEntity} from "../interfaces/StoreResponse";
import {
    FormControl,
    FormGroup,
    FormsModule,
    ReactiveFormsModule,
    Validators
} from "@angular/forms";
import {
    AddStoreLocationModalComponent
} from "./add-store-location-modal/add-store-location-modal.component";
import {CreateLocation} from "../interfaces/CreateLocation";

@Component({
    selector: 'app-one-store',
    standalone: true,
    imports: [
        MainContentComponent,
        AsyncPipe,
        TranslatePipe,
        RouterLink,
        DatePipe,
        FormsModule,
        ReactiveFormsModule,
        AddStoreLocationModalComponent
    ],
    templateUrl: './one-store.component.html',
    styles: ``
})
export class OneStoreComponent {
    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage)
    store$!: Observable<StoreEntity>;
    loader$!: Observable<boolean>;
    storeId!: number;
    showAddLocationModal: boolean = false;
    selectedLocationId!: number;
    tableColumns: string[] = [
        '#',
        'Name',
        'Status',
        'Last updated',
        'Actions',
    ];
    showEditStoreModal: boolean = false;
    showEditLocationModal: boolean = false;
    editStoreForm: FormGroup = new FormGroup({
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
    editLocationForm: FormGroup = new FormGroup({
        nameAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        nameEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        isActive: new FormControl(true,),
    });

    constructor(
        private loaderService: LoaderService,
        private store: Store<State>,
        private activeRouter: ActivatedRoute,
        private storesService: StoresService,
        private router: Router,
    ) {
        this.loader$ = this.loaderService.isLoading;
        this.selectLanguage$ = this.store.select(selectLanguage);
    }

    ngOnInit(): void {
        this.initPageParams();
    }

    getData() {
        this.store$ = this.storesService.getStore(this.storeId);
    }

    showAddLocationModalF() {
        this.showAddLocationModal = true;
    }

    hideAddLocationModal() {
        this.showAddLocationModal = false;
    }

    onSubmitForm($event: CreateLocation) {
        // todo: set default active on backend to true
        $event.storeId = this.storeId
        $event.isActive = true
        this.storesService.addLocationToStore(this.storeId, $event).subscribe({
            next: (res) => {
                this.getData()
                this.hideAddLocationModal()
            },
            error: (err) => {
                }
        })
    }

    changeEditStoreModalVisibility(b: boolean) {
        this.showEditStoreModal = b;
    }

    chaneEditLocationModalVisibility(b: boolean) {
        this.showEditLocationModal = b;
        b ? '' : this.selectedLocationId = 0
    }

    onEditStoreFormSubmit() {
        if (this.editStoreForm.invalid) {
            return;
        }
        const {
            nameAr,
            nameEn,
            cityAr,
            cityEn,
            isActive
        } = this.editStoreForm.value

        this.storesService.updateStore(this.storeId, {
            nameAr,
            nameEn,
            cityAr,
            cityEn,
            isActive
        }).subscribe({
            next: () => {
                this.editStoreForm.reset()
                this.getData()
                this.changeEditStoreModalVisibility(false)
            },
            error: (r) => {
                }
        })
    }

    onShowEditStoreModal() {
        this.store$.subscribe((data) => {
            this.editStoreForm.controls['nameEn'].setValue(data.nameEn)
            this.editStoreForm.controls['nameAr'].setValue(data.nameAr)
            this.editStoreForm.controls['cityEn'].setValue(data.cityEn)
            this.editStoreForm.controls['cityAr'].setValue(data.cityAr)
            this.editStoreForm.controls['isActive'].setValue(data.isActive)
        })
        this.changeEditStoreModalVisibility(true)
    }

    onEditLocationFormSubmit() {
        if (this.editLocationForm.invalid) {
            return;
        }
        const {
            nameAr,
            nameEn,
            isActive
        } = this.editLocationForm.value

        this.storesService.updateLocationInStore(this.storeId, this.selectedLocationId,{
            nameAr,
            nameEn,
            storeId:this.storeId,
            isActive
        }).subscribe({
            next: () => {
                this.editLocationForm.reset()
                this.getData()
                this.chaneEditLocationModalVisibility(false)
            },
            error: (r) => {
                }
        })
    }

    onShowEditLocationModal(location: any) {
        this.selectedLocationId = location.id
        this.editLocationForm.controls['nameEn'].setValue(location.nameEn)
        this.editLocationForm.controls['nameAr'].setValue(location.nameAr)
        this.editLocationForm.controls['isActive'].setValue(location.isActive)
        this.chaneEditLocationModalVisibility(true)
    }

    private initPageParams() {
        // this.loaderService.show()
        this.activeRouter.params.subscribe(params => {
            this.storeId = params['id'];
            this.storeId && this.getData()
        })
    }
}
