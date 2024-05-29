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
     sort!: string;
     ref!: number;
     phase!: string;
     user!: number;
     status!: string;
     storeId!: number;

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

    onSearchChanged($event: string) {
        this.searchName = $event;
        this.router.navigate([], {
            relativeTo: this.activeRouter,
            queryParams: {name: $event},
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
