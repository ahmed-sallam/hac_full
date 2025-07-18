import {Component, inject, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../state/reducers";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {Observable} from "rxjs";
import {LangState} from "../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../state/selectors/lang.selectors";
import {selectUser} from "../../../state/selectors/auth.selectors";
import {Pageable} from "../../inventory/stores/interfaces/StoreResponse";
import {SupplierQuotationService} from "./supplier-quotation.service";
import {
  SupplierQuotationResponse,
  SupplierQuotationShort
} from "./interfaces/SupplierQuotationResponse";
import {AsyncPipe, DatePipe} from "@angular/common";
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
  selector: 'app-supplier-quotation',
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    TranslatePipe,
    SearchInputComponent,
    RouterLink,
    DatePipe,
    PagenationComponent
  ],
  templateUrl: './supplier-quotation.component.html',
  styles: ``
})
export class SupplierQuotationComponent implements OnInit {

  store: Store<State> = inject(Store<State>);
  activeRouter: ActivatedRoute = inject(ActivatedRoute);
  router: Router = inject(Router);
  selectLanguage$: Observable<LangState> = this.store.select(selectLanguage);
  selectUser$: Observable<any> = this.store.select(selectUser);
  pageable!: Pageable;
  tableColumns: string[] = [
    'Id',
    'RFPQ',
    'Date',
    'Supplier',
    'Is Local',
    'Total',
    'User',
  ];
  currentPage!: number;
  pageSize!: number;
  searchName: string = '';
  sort!: string|undefined;
  isLocal!: boolean|undefined;
  supplierRef!: string|undefined;
  ref!: number|undefined;
  user!: number|undefined;
  supplier!: number|undefined;
  date!: Date|string|undefined;

  all: boolean = true;
  supplierQuotationService: SupplierQuotationService = inject(SupplierQuotationService);
  supplierQuotationResponse!: SupplierQuotationResponse;
  supplierQuotations: SupplierQuotationShort[]=[];
  ngOnInit(): void {
    this.initPageParams();
    this.getData();
  }
  getData() {
    this.supplierQuotationService
        .getQuotations(
            this.currentPage, this.sort, this.pageSize,
            this.searchName, this.ref, this.supplier,
            this.user, this.date, this.supplierRef, this.isLocal
        )
        .subscribe((res) => {
          this.supplierQuotationResponse = res;
          this.pageable = res.pageable;
          this.supplierQuotations = res.content;
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
    return new Array(this.supplierQuotationResponse.totalPages);
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
    this.date = undefined
    this.user = undefined
    this.supplier = undefined
    this.supplierRef = undefined
    this.isLocal = undefined
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
      this.date = params["date"];
      this.user = params["user"];
      this.supplier = params["supplier"];
      this.supplierRef = params["supplierRef"];
      this.isLocal = params["isLocal"];
    });
  }


}
