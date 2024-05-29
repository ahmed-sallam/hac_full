import {Component, inject, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../state/reducers";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {Observable} from "rxjs";
import {LangState} from "../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../state/selectors/lang.selectors";
import {Pageable} from "../../inventory/stores/interfaces/StoreResponse";
import {MaterialRequestService} from "./material-request.service";
import {
    MaterialRequestResponse,
    MaterialRequestShort
} from "./interfaces/MaterialRequestResponse";
import {AsyncPipe, DatePipe, NgClass} from "@angular/common";
import {
    MainContentComponent
} from "../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../pipes/translate.pipe";
import {
    SearchInputComponent
} from "../../components/search-input/search-input.component";
import {selectUser} from "../../../state/selectors/auth.selectors";

@Component({
    selector: 'app-material-request',
    standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        TranslatePipe,
        RouterLink,
        SearchInputComponent,
        NgClass,
        DatePipe
    ],
    templateUrl: './material-request.component.html',
    styles: ``
})
export class MaterialRequestComponent implements OnInit {
    store: Store<State> = inject(Store<State>);
    activeRouter: ActivatedRoute = inject(ActivatedRoute);
    router: Router = inject(Router);
    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage);
    selectUser$: Observable<any> = this.store.select(selectUser);
    pageable!: Pageable;
    tableColumns: string[] = [
        'Internal Ref',
        'MR number',
        'Date',
        'Store',
        'Status',
        'Phase',
        'User',
    ];
    currentPage!: number;
    pageSize!: number;
    searchName: string = '';
     sort!: string|undefined;
     ref!: number|undefined;
     phase!: string|undefined;
     user!: number|undefined;
     status!: string|undefined;
     storeId!: number|undefined;

     all: boolean = true;

    materialRequestService: MaterialRequestService = inject(MaterialRequestService);
     materialRequestResponse!: MaterialRequestResponse;
     materialRequests: MaterialRequestShort[] = [];

    ngOnInit(): void {
        this.initPageParams();
        this.getData();
    }

    getData() {
        this.materialRequestService
            .getMaterialRequests(
                this.currentPage, this.sort, this.pageSize,
                this.searchName, this.ref, this.storeId,
                this.user, this.phase, this.status
            )
            .subscribe((res) => {
                this.materialRequestResponse = res;
                this.pageable = res.pageable;
                this.materialRequests = res.content;
            });
    }

    onSearchChanged($event: any) {
        this.searchName = $event["search"];
        const params = {...$event}
        this.router.navigate([], {
            relativeTo: this.activeRouter,
            queryParams: params,
            queryParamsHandling: 'merge',
        });
        this.getData();
    }
    generatePageArray() {
        return new Array(this.materialRequestResponse.totalPages);
    }
    onPageChanged($index: number) {
        if ($index == -1) {
            return
        }
        if (+this.currentPage == $index) {
            return
        }
        this.currentPage = $index;
        this.router.navigate([], {
            relativeTo: this.activeRouter,
            queryParams: {page: $index},
            queryParamsHandling: 'merge',
        });
        this.getData();
    }

    resetParamsAndSearch(){
        this.searchName =  '';
        this.currentPage =  0;
        this.pageSize =  80;
        this.sort = undefined
        this.ref = undefined
        this.phase = undefined
        this.user = undefined
        this.status = undefined
        this.storeId = undefined
    }

    getMyRequests(id: number){
        this.user = id;
        this.all= false;
        this.onSearchChanged({search:'', user: id})
    }
    getAll(){
        this.resetParamsAndSearch()
        this.all= true;
        this.onSearchChanged({search:''})
    }

    private initPageParams() {
        this.activeRouter.queryParams.subscribe((params) => {
            this.searchName = params['name'] || '';
            this.currentPage = params['page'] || 0;
            this.pageSize = params['size'] || 80;
            this.sort = params["sort"];
            this.ref = params["sort"];
            this.phase = params["sort"];
            this.user = params["sort"];
            this.status = params["sort"];
            this.storeId = params["sort"];
        });
    }
}
