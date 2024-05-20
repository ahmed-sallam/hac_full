import {Component, inject, OnInit} from '@angular/core';
import {AsyncPipe, JsonPipe, NgClass} from "@angular/common";
import {MainContentComponent} from "../../../components/main-content/main-content.component";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {catchError, map, Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {MachinePartsService} from "../../machine-parts/machine-parts.service";
import {MachinePartEntity, MachinePartResponse} from "../../machine-parts/interfaces/MachinePartResponse";
import {SelectWithSearchComponent} from "./select-with-search/select-with-search.component";
import {BrandsService} from "../../brands/brands.service";
import {BrandEntity, BrandsResponse} from "../../brands/BrandsResponse";
import {CountriesService} from "../../../core/countries/countries.service";
import {Country} from "../../../core/countries/interfaces/Country";
import {FilesService} from "../../../../files.service";
import {HttpEventType, HttpResponse} from "@angular/common/http";
import {ProductsService} from "../products.service";
import {CreateProduct} from "../interfaces/CreateProduct";
import {Router} from "@angular/router";
import {SelectSetItemsModalComponent} from "./select-set-items-modal/select-set-items-modal.component";
import {SelectAlternativeModalComponent} from "./select-alternatice-modal/select-alternative-modal.component";
import {MachineryService} from "../../machinery/machinery.service";
import {
    MachineryModelEntity,
    MachineryResponse,
    MachineryTypeEntity
} from "../../machinery/interfaces/MachineryResponse";

@Component({
    selector: 'app-create-product',
    standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        ReactiveFormsModule,
        TranslatePipe,
        JsonPipe,
        SelectWithSearchComponent,
        SelectSetItemsModalComponent,
        SelectAlternativeModalComponent,
        NgClass,
    ],
    templateUrl: './create-product.component.html',
    styles: ``
})
export class CreateProductComponent implements OnInit {
    productImageUploadProgress: number = 0;
    partImageUploadProgress: number = 0;
    productImageUrl!: string | any;
    partImageUrl!: string | any;
    private newProductId: number | any;
    tempProductImage: any;
    tempPartImage: any;

    ngOnInit(): void {
        this.getMachineParts()
        this.getBrands()
        this.getCountryOptions()
        this.getMachinery()
    }


    store: Store<State> = inject(Store<State>)
    // storesService: StoresService = inject(StoresService)
    machinePartsService: MachinePartsService = inject(MachinePartsService);
    brandsService: BrandsService = inject(BrandsService);
    countriesService: CountriesService = inject(CountriesService);
    filesService: FilesService = inject(FilesService);
    productsService: ProductsService = inject(ProductsService)
    machineryService: MachineryService = inject(MachineryService)

    router: Router = inject(Router)

    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage)
    machinePartOptions$!: Observable<MachinePartEntity[]>;
    brandOptions$!: Observable<BrandEntity[]>;
    countryOptions$!: Observable<Country[]>;
    countryOptionsBefore$!: Observable<Country[]>;
    subBrandOptions$!: Observable<BrandEntity[]>;
    machineryOptions$ !: Observable<MachineryTypeEntity[]>;
    machineryModelOptions$ !: Observable<MachineryModelEntity[]>;
    productSets: any[] = []
    alternatives: any[] = [];
    related: any[] = [];
    formGroup: FormGroup = new FormGroup({
        productNumber: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        descriptionAr: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        descriptionEn: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
        ]),
        image: new FormControl('', [Validators.required
        ]),
        partImage: new FormControl('', [Validators.required
        ]),
        minQuantity: new FormControl(0, [Validators.required, Validators.min(0)]),
        sellQuantity: new FormControl(0, [Validators.required, Validators.min(0)]),
        sellIndividual: new FormControl(0, [Validators.required, Validators.min(0)]),
        buyQuantity: new FormControl(0, [Validators.required, Validators.min(0)]),
        buyIndividual: new FormControl(0, [Validators.required, Validators.min(0)]),
        isOriginal: new FormControl(true, []),
        unit: new FormControl('PIECE', []),
        machinePart: new FormControl('', [Validators.required]),
        mainBrand: new FormControl('', [Validators.required]),
        subBrand: new FormControl('', [Validators.required]),
        country: new FormControl('', [Validators.required]),
        machineryType: new FormControl('', [Validators.required]),
        machineryModel: new FormControl('', [Validators.required]),


    });

    UnitTypes = [
        'PIECE',
        'SET'
    ]
    refreshSubBrand: boolean = true;
    refreshMachineryModel: boolean = true;
    showSelectSetItemsModal: boolean = false;
    showSelectAlternatives: boolean = false;
    showSelectRelated: boolean = false;
    showSuccessModal: boolean = false;


    imageValidator(file: any): boolean {
        const allowedMimeTypes = ['image/jpeg', 'image/png', 'image/gif'];
        console.log("file.type", file)
        return !allowedMimeTypes.includes(file.type);
        // Valid image type
    }

    onSubmitForm() {

        this.formGroup.markAllAsTouched()
        if (this.formGroup.invalid) {
            return;
        } else {
            const product: CreateProduct = this.formGroup.value
            product.image = this.productImageUrl
            product.partImage = this.partImageUrl
            if (this.productSets.length < 2 && this.formGroup.get('unit')?.value == 'SET') {
                return alert('Please add at least 2 products') // todo: change to toast
            }
            product.productSets = this.productSets.map((p: any) => {
                return {
                    productId: p.productId,
                    quantity: p.quantity,
                    isRestricted: p.isRestricted
                }
            })
            product.alternatives = this.alternatives.map((p: any) => {
                return {
                    product2Number: p.productNumber,
                }
            })
            product.related = this.related.map((p: any) => {
                return {
                    product2Number: p.productNumber,
                    isRestricted: p.isRestricted
                }
            })
            this.addProduct(product)
        }

    }

    addProduct(product: CreateProduct) {
        this.productsService.addProduct(product).subscribe({
            next: (res) => {
                // this.goToProducts()
                this.showSuccessModal = true
                this.newProductId = res
            },
            error: (err) => {
                console.log("err", err)
            }
        })

    }

    goToProducts() {
        this.router.navigate(['/dashboard/inventory/products'])
    }

    getMachineParts(
        page: number = 0,
        size: number = 15,
        name: string = '',
        isActive: boolean = true
    ) {
        this.machinePartOptions$ = this.machinePartsService.getMachineParts(
            page,
            size,
            name,
            isActive
        ).pipe(
            map((res: MachinePartResponse) => {
                console.log("res1", res)
                return res.content
            }),
            catchError((err) => {
                console.log("err", err)
                return []
            })
        )
    }

    getBrands(
        page: number = 0,
        size: number = 15,
        name: string = '',
        isActive: boolean = true
    ) {
        this.brandOptions$ = this.brandsService.getBrands(
            page,
            size,
            name,
            isActive
        ).pipe(
            map((res: BrandsResponse) => {
                console.log("res1", res)
                return res.content
            }),
            catchError((err) => {
                console.log("err", err)
                return []
            })
        )
    }

    getMachinery(
        page: number = 0,
        size: number = 15,
        name: string = '',
        isActive: boolean = true
    ) {
        this.machineryOptions$ = this.machineryService.getMachinery(
            page,
            size,
            name,
            isActive
        ).pipe(
            map((res: MachineryResponse) => {
                console.log("res1", res)
                return res.content
            }),
            catchError((err) => {
                console.log("err", err)
                return []
            })
        )
    }

    getCountryOptions() {
        this.countryOptions$ = this.countriesService.getCountries().pipe(
            map((res: Country[]) => {
                console.log("res1", res)
                return res
            }),
            catchError((err) => {
                console.log("err", err)
                return []
            })
        )
        this.countryOptionsBefore$ = this.countryOptions$
    }

    onProductImageChange($event: Event | any) {
        this.imageValidator($event.target.files[0]) ? this.formGroup.get('image')?.setErrors({'invalidFileType': true}) : this.formGroup.get('image')?.setErrors(null)
        console.log("onProductImageChange", $event.target.files[0]['type'])
      this.loadProductImageToPreview($event.target.files[0])
        this.uploadImage($event.target.files[0], 'product')
    }

    loadProductImageToPreview(file: File): void {
        const reader: FileReader = new FileReader();
         reader.onload = (e: any) => {
             this.tempProductImage =  e.target.result;
        }
        reader.readAsDataURL(file);
    }
    loadPartImageToPreview(file: File): void {
        const reader: FileReader = new FileReader();
         reader.onload = (e: any) => {
             this.tempPartImage =  e.target.result;
        }
        reader.readAsDataURL(file);
    }
    uploadImage(file: File, into: string) {
        this.filesService.uploadImage(file, 'products')
            .subscribe(
                {
                    next: (event) => {
                        if (event.type === HttpEventType.UploadProgress) {
                            let percentNumber: number = Math.round(100 * event.loaded / event.total);
                            console.log('Upload progress: ' + percentNumber + '%');
                            if (into == 'product') {
                                this.productImageUploadProgress = percentNumber;
                            } else if (into == 'part') {
                                this.partImageUploadProgress = percentNumber;
                            }
                        } else if (event instanceof HttpResponse) {
                            console.log('File is completely uploaded!', event.body);
                            if (into == 'product') {
                                this.productImageUrl = event.body;
                                // this.productImageUploadProgress = 0;
                            } else if (into == 'part') {
                                this.partImageUrl = event.body;
                                // this.partImageUploadProgress = 0;
                            }
                        }
                    }
                    ,
                    error: (error) => {
                        console.log('Upload failed: ', error);
                    }
                }
            );
    }

    onMachinePartImageChange($event: Event | any) {
        this.imageValidator($event.target.files[0]) ? this.formGroup.get('partImage')?.setErrors({'invalidFileType': true}) : this.formGroup.get('partImage')?.setErrors(null)
        this.loadPartImageToPreview($event.target.files[0])

        console.log("onMachinePartImageChange", $event)
        this.uploadImage($event.target.files[0], 'part')

    }

    onItemSelected($event: any, key: string) {
        if ($event == null) {
            this.formGroup.get(key)?.setValue('')
            if (key == 'mainBrand') {
                this.resetSubBrandOptions()
                this.resetMachineryModelOptions()
            }
            if (key == 'machineryType') {
                this.resetMachineryModelOptions()
            }
        } else {
            this.formGroup.get(key)?.setValue($event.id)
            if (key == 'mainBrand') {
                this.resetSubBrandOptions()
                this.subBrandOptions$ = this.brandOptions$.pipe(
                    map((res: BrandEntity[]) => {
                        return res.filter((b: BrandEntity) => {
                            return b.id == $event.id
                        }).map((b: BrandEntity) => b.subBrands).flat()
                    })
                )
                if (this.formGroup.get('machineryType')?.value != '') {
                    this.resetMachineryModelOptions()
                    this.machineryModelOptions$ = this.machineryOptions$.pipe(
                        map((res: MachineryTypeEntity[]) => {
                            return res.filter((i: MachineryTypeEntity) =>
                                i.id == this.formGroup.get('machineryType')?.value
                            ).map((b: MachineryTypeEntity) =>
                                b.machineryModels
                            ).flat().filter(
                                (i2: MachineryModelEntity) =>
                                    i2.brand?.id == this.formGroup.get('mainBrand')?.value
                            )
                        })
                    );
                }
            }

            if (key == 'machineryType') {
                this.resetMachineryModelOptions()
                this.machineryModelOptions$ = this.machineryOptions$.pipe(
                    map((res: MachineryTypeEntity[]) => {
                        return res.filter((i: MachineryTypeEntity) =>
                            i.id == this.formGroup.get('machineryType')?.value
                        ).map((b: MachineryTypeEntity) =>
                            b.machineryModels
                        ).flat().filter(
                            (i2: MachineryModelEntity) =>
                                i2.brand?.id == this.formGroup.get('mainBrand')?.value
                        )
                    })
                );
            }
        }
    }

    resetSubBrandOptions() {
        this.subBrandOptions$ = new Observable<BrandEntity[]>()
        this.formGroup.get('subBrand')?.setValue('')
        this.refreshSubBrand = !this.refreshSubBrand
        setTimeout(() => {
            this.refreshSubBrand = !this.refreshSubBrand
        }, 5);
    }

    resetMachineryModelOptions() {
        this.machineryModelOptions$ = new Observable<MachineryModelEntity[]>()
        this.formGroup.get('machineryModel')?.setValue('')
        this.refreshMachineryModel = !this.refreshMachineryModel
        setTimeout(() => {
            this.refreshMachineryModel = !this.refreshMachineryModel
        }, 5);
    }

    searchMachineParts($event: string) {
        this.getMachineParts(0, 15, $event.trim(), true);
    }

    searchBrands($event: string) {
        this.getBrands(0, 15, $event.trim(), true);
    }

    searchMachinery($event: string) {
        this.getMachinery(0, 15, $event.trim(), true);
    }

    searchCountries($event: string) {
        console.log('ss')
        this.countryOptions$ = this.countryOptionsBefore$.pipe(
            map((res: Country[]) => {
                return res.filter((c: Country) => {
                    return c.nameEn.toLowerCase().includes($event.toLowerCase()) || c.nameAr.includes($event)
                })
            })
        );
    }

    onSelectIsSet() {
        this.showSelectSetItemsModal = true;
    }

    onSubmitProductSet($event: any[]) {
        this.productSets = $event;
        this.showSelectSetItemsModal = false
    }

    onSubmitAlternatives($event: any[]) {
        this.alternatives = $event;
        this.showSelectAlternatives = false
    }
    onSubmitRelated($event: any[]) {
        this.related = $event;
        this.showSelectRelated = false
    }

    hideSelectSetItemsModal() {
        this.showSelectSetItemsModal = false
        this.productSets = []
    }

    hideSelectAlternatives() {
        this.showSelectAlternatives = false
        this.alternatives = []
    }
    hideSelectRelated() {
        this.showSelectRelated = false
        this.related = []
    }

    cancelCreateProduct() {
        this.formGroup.reset();
        this.goToProducts()
    }

    goToAddStock() {
        this.router.navigate(['/dashboard/inventory/stock/create'], {
            queryParams: {productId: this.newProductId}
        })
    }
}
