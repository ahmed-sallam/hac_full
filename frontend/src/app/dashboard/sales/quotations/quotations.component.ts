import { Component, inject, OnInit } from "@angular/core";
import { ActivatedRoute, Router, RouterLink } from "@angular/router";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { LangState } from "../../../state/reducers/lang.reducer";
import { selectUser } from "../../../state/selectors/auth.selectors";
import { selectLanguage } from "../../../state/selectors/lang.selectors";
import { Pageable } from "../../inventory/brands/BrandsResponse";
import { State } from "../../../state/reducers";
import { QuotationsService } from "./quotations.service";
import {
  QuotationShort,
  QuotationsResponse,
} from "./interfaces/QuotationsResponse";
import { AsyncPipe, DatePipe, NgClass } from "@angular/common";
import { TranslatePipe } from "../../../pipes/translate.pipe";
import { MainContentComponent } from "../../components/main-content/main-content.component";
import { PagenationComponent } from "../../components/pagenation/pagenation.component";
import { SearchInputComponent } from "../../components/search-input/search-input.component";

@Component({
  selector: "app-quotations",
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    TranslatePipe,
    RouterLink,
    SearchInputComponent,
    NgClass,
    DatePipe,
    PagenationComponent,
  ],
  templateUrl: "./quotations.component.html",
  styles: ``,
})
export class QuotationsComponent implements OnInit {
  store: Store<State> = inject(Store<State>);
  activeRouter: ActivatedRoute = inject(ActivatedRoute);
  router: Router = inject(Router);
  selectLanguage$: Observable<LangState> = this.store.select(selectLanguage);
  selectUser$: Observable<any> = this.store.select(selectUser);
  pageable!: Pageable;
  quotationsService: QuotationsService = inject(QuotationsService);
  quotationResponse!: QuotationsResponse;
  quotations: QuotationShort[] = [];
  tableColumns: string[] = [
    "Internal Ref",
    "SALQ number",
    "Date",
    "Customer",
    "Status",
    "Phase",
    "User",
  ];

  currentPage!: number;
  pageSize!: number;
  searchName: string = "";
  sort!: string | undefined;
  ref!: number | undefined;
  user!: number | undefined;
  status!: string | undefined;
  all: boolean = true;

  ngOnInit(): void {
    this.initPageParams();
    this.getData();
  }

  getData() {
    this.quotationsService
      .getQuotations(
        this.currentPage,
        this.sort,
        this.pageSize,
        this.searchName,
        this.ref,
        this.user,
        this.status,
      )
      .subscribe((res) => {
        this.quotationResponse = res;
        this.pageable = res.pageable;
        this.quotations = res.content;
      });
  }

  onSearchChanged($event: any) {
    this.searchName = $event["search"];
    const params = { ...$event };
    this.router.navigate([], {
      relativeTo: this.activeRouter,
      queryParams: params,
      queryParamsHandling: "merge",
    });
    this.getData();
  }
  generatePageArray() {
    return new Array(this.quotationResponse.totalPages);
  }
  onPageChanged($index: number) {
    if ($index == -1) {
      return;
    }
    if (+this.currentPage == $index) {
      return;
    }
    this.currentPage = $index;
    this.router.navigate([], {
      relativeTo: this.activeRouter,
      queryParams: { page: $index },
      queryParamsHandling: "merge",
    });
    this.getData();
  }

  resetParamsAndSearch() {
    this.searchName = "";
    this.currentPage = 0;
    this.pageSize = 80;
    this.sort = undefined;
    this.ref = undefined;
    this.user = undefined;
    this.status = undefined;
  }

  getMyRequests(id: number) {
    this.user = id;
    this.all = false;
    this.onSearchChanged({ search: "", user: id });
  }
  getAll() {
    this.resetParamsAndSearch();
    this.all = true;
    this.onSearchChanged({ search: "" });
  }

  private initPageParams() {
    this.activeRouter.queryParams.subscribe((params) => {
      this.searchName = params["search"] || "";
      this.currentPage = params["page"] || 0;
      this.pageSize = params["size"] || 80;
      this.sort = params["sort"];
      this.ref = params["ref"];
      this.user = params["user"];
      this.status = params["status"];
    });
  }
}
