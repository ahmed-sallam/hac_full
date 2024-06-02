import {Component, inject} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../state/reducers";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {Observable} from "rxjs";
import {LangState} from "../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../state/selectors/lang.selectors";
import {selectUser} from "../../../state/selectors/auth.selectors";
import {Pageable} from "../../inventory/stores/interfaces/StoreResponse";
import {RfpqService} from "./rfpq.service";
import {RFPQResponse, RFPQShort} from "./interfaces/RFPQResponse";
import {AsyncPipe, DatePipe, NgClass} from "@angular/common";
import {
  MainContentComponent
} from "../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../pipes/translate.pipe";
import {
  SearchInputComponent
} from "../../components/search-input/search-input.component";
import {
  PagenationComponent
} from "../../components/pagenation/pagenation.component";

@Component({
  selector: 'app-rfpq',
  standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        TranslatePipe,
        RouterLink,
        SearchInputComponent,
        DatePipe,
        NgClass,
        PagenationComponent
    ],
  templateUrl: './rfpq.component.html',
  styles: ``
})
export class RfpqComponent {

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
  rfpqService: RfpqService = inject(RfpqService);
  rfpqResponse !: RFPQResponse;
  rfpqs: RFPQShort[] = [];
  ngOnInit(): void {
    this.initPageParams();
    this.getData();
  }

  getData() {
    this.rfpqService
        .getRFPQs(
            this.currentPage, this.sort, this.pageSize,
            this.searchName, this.ref, this.storeId,
            this.user, this.phase, this.status
        )
        .subscribe((res) => {
          this.rfpqResponse = res;
          this.pageable = res.pageable;
          this.rfpqs = res.content;
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
    return new Array(this.rfpqResponse.totalPages);
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
      this.searchName = params['search'] || '';
      this.currentPage = params['page'] || 0;
      this.pageSize = params['size'] || 80;
      this.sort = params["sort"];
      this.ref = params["ref"];
      this.phase = params["phase"];
      this.user = params["user"];
      this.status = params["status"];
      this.storeId = params["storeId"];
    });
  }

}
