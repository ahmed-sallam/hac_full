import {Component, inject, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../state/reducers";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {Observable} from "rxjs";
import {LangState} from "../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../state/selectors/lang.selectors";
import {selectUser} from "../../../state/selectors/auth.selectors";
import {Pageable} from "../../inventory/stores/interfaces/StoreResponse";
import {AsyncPipe, DatePipe} from "@angular/common";
import {
    MainContentComponent
} from "../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../pipes/translate.pipe";
import {BidSummaryService} from "./bid-summary.service";
import {
    BidSummaryResponse,
    BidSummaryShort
} from "./interfaces/BidSummaryResponse";
import {
    SearchInputComponent
} from "../../components/search-input/search-input.component";
import {
    PagenationComponent
} from "../../components/pagenation/pagenation.component";

@Component({
    selector: 'app-bid-summary',
    standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        TranslatePipe,
        RouterLink,
        SearchInputComponent,
        DatePipe,
        PagenationComponent
    ],
    templateUrl: './bid-summary.component.html',
    styles: ``
})
export class BidSummaryComponent implements OnInit {

    store: Store<State> = inject(Store<State>);
    activeRouter: ActivatedRoute = inject(ActivatedRoute);
    router: Router = inject(Router);
    bisSummaryService: BidSummaryService = inject(BidSummaryService);
    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage);
    selectUser$: Observable<any> = this.store.select(selectUser);
    pageable!: Pageable;
    tableColumns: string[] = [
        'Internal Ref',
        'RFPQ',
        'BS number',
        'Date',
        'Status',
        'Phase',
        'User',
    ];
    currentPage!: number;
    pageSize!: number;
    searchName: string = '';
    sort!: string | undefined;
    ref!: number|undefined;
    phase!: string|undefined;
    user!: number|undefined;
    status!: string|undefined;

    all: boolean = true;

    bidSummaryResponse!: BidSummaryResponse;
    bidSummaries: BidSummaryShort[] = [];

    ngOnInit(): void {
        this.initPageParams();
        this.getData();
    }
    getAll(){
        this.resetParamsAndSearch()
        this.all= true;
        this.onSearchChanged({search:''})
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
        return new Array(this.bidSummaryResponse.totalPages);
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

    getMyRequests(id: number){
        this.user = id;
        this.all= false;
        this.onSearchChanged({search:'', user: id})
    }

    private initPageParams() {
        this.activeRouter.queryParams.subscribe((params) => {
            this.searchName = params['search'] || '';
            this.currentPage = params['page'] || 0;
            this.pageSize = params['size'] || 80;
            this.sort = params["sort"];
            this.ref = params["ref"];
            this.phase = params["phase"];
            this.user = params["user"];
            this.status = params["status"];
        });
    }

    private getData() {
        this.bisSummaryService
            .getBidSummaries(
                this.currentPage, this.sort, this.pageSize,
                this.searchName, this.ref,
                this.user, this.phase, this.status
            )
            .subscribe((res) => {
                this.bidSummaryResponse = res;
                this.pageable = res.pageable;
                this.bidSummaries = res.content;
            });
    }

    private resetParamsAndSearch(){
        this.searchName =  '';
        this.currentPage = 0;
        this.pageSize = 80;
        this.sort = undefined
        this.ref = undefined
        this.phase = undefined
        this.user = undefined
        this.status = undefined
    }

}
