import {Component, OnInit} from '@angular/core';
import {LoaderService} from "../../../components/loader/loader.service";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {BrandsService} from "../brands.service";
import {BrandEntity} from "../BrandsResponse";
import {AsyncPipe, DatePipe} from "@angular/common";
import {MainContentComponent} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {
    AddStoreLocationModalComponent
} from "../../stores/one-store/add-store-location-modal/add-store-location-modal.component";
import {CreateBrand} from "../interfaces/CreateBrand";
import {AddSubBrandModalComponent} from "../create-brand/add-sub-brand-modal/add-sub-brand-modal.component";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";

@Component({
    selector: 'app-one-brand',
    standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        TranslatePipe,
        RouterLink,
        DatePipe,
        AddStoreLocationModalComponent,
        AddSubBrandModalComponent,
        FormsModule,
        ReactiveFormsModule
    ],
    templateUrl: './one-brand.component.html',
    styles: ``
})
export class OneBrandComponent implements OnInit {
    subBrandId!: number;

    constructor(
        private loaderService: LoaderService,
        private store: Store<State>,
        private activeRouter: ActivatedRoute,
        private brandsService: BrandsService,
        private router: Router,
    ) {
        this.loader$ = this.loaderService.isLoading;
        this.selectLanguage$ = this.store.select(selectLanguage);
    }

    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage)
    brands$!: Observable<BrandEntity>;
    loader$!: Observable<boolean>;
    brandId!: number;
    showAddSubBrandModal: boolean = false;
    showEditBrandModal: boolean = false;
    showEditSubBrandModal: boolean = false;

    tableColumns: string[] = [
        '#',
        'Name',
        'Code',
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
            this.brandId = params['id'];
            this.brandId && this.getData()
        })
    }

    getData() {
        this.brands$ = this.brandsService.getBrandById(this.brandId);
    }

    showAddSubBrandModalF() {
        this.showAddSubBrandModal = true;

    }

    hideAddSubBrandModal() {
        this.showAddSubBrandModal = false;
    }

    onSubmitForm($event: CreateBrand) {
        // todo: set default active on backend to true
        $event.brandId = this.brandId
        $event.isActive = true
        this.brandsService.addBrand($event).subscribe({
            next: (res) => {
                console.log("res", res)
                this.getData()
                this.hideAddSubBrandModal()
            },
            error: (err) => {
                console.log("err", err)
            }
        })
    }
    editBrandForm: FormGroup = new FormGroup({
        nameAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        nameEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        isActive: new FormControl(true,),
    });
    editSubBrandForm: FormGroup = new FormGroup({
        nameAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        nameEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        code: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        isActive: new FormControl(true,),
    });

    changeEditBrandVisibility(b: boolean) {
        this.showEditBrandModal = b;
    }
    changeEditSubBrandVisibility(b: boolean) {
        this.showEditSubBrandModal = b;
        b? '' : this.subBrandId=0
    }
    onSubmitEditBrandForm() {
        if (this.editBrandForm.invalid) {
            return;
        }
        const {
            nameAr,
            nameEn,
            isActive
        } = this.editBrandForm.value

        this.brandsService.updateBrand(this.brandId,  {   nameAr,
            nameEn,
            isActive}).subscribe({
            next: (res) => {
                console.log("res", res)
                this.editBrandForm.reset()
                this.getData()
                this.changeEditBrandVisibility(false)
            },
            error: (err) => {
                console.log("err", err)
            }
        })
    }
    onSubmitEditSubBrandForm() {
        if (this.editSubBrandForm.invalid) {
            return;
        }
        const {
            nameAr,
            nameEn,
            code,
            isActive
        } = this.editSubBrandForm.value
        this.brandsService.updateBrand(this.subBrandId,  {     nameAr,
            nameEn,
            brandId:this.brandId,
            code,
            isActive}).subscribe({
            next: (res) => {
                console.log("res", res)
                this.editSubBrandForm.reset()
                this.getData()
                this.changeEditSubBrandVisibility(false)
            },
            error: (err) => {
                console.log("err", err)
            }
        })
    }
    onshowEditSubBrandModal($event: BrandEntity) {
        this.subBrandId = $event.id
        this.editSubBrandForm.setValue({
            nameAr: $event.nameAr,
            nameEn: $event.nameEn,
            code: $event.code,
            isActive: $event.isActive
        })
        this.changeEditSubBrandVisibility(true)
    }
    onshowEditBrandModal() {
        this.brands$.subscribe((data) => {
            this.editBrandForm.controls['nameEn'].setValue(data.nameEn)
            this.editBrandForm.controls['nameAr'].setValue(data.nameAr)
            this.editBrandForm.controls['isActive'].setValue(data.isActive)
        })
        this.changeEditBrandVisibility(true)
    }
}
