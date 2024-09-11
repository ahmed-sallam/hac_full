import { AsyncPipe, NgClass } from "@angular/common";
import { Component, inject } from "@angular/core";
import { ActivatedRoute, Router, RouterLink } from "@angular/router";
import { TranslatePipe } from "../../../pipes/translate.pipe";
import { MainContentComponent } from "../../components/main-content/main-content.component";
import { PagenationComponent } from "../../components/pagenation/pagenation.component";
import { SearchInputComponent } from "../../components/search-input/search-input.component";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { LangState } from "../../../state/reducers/lang.reducer";
import { selectLanguage } from "../../../state/selectors/lang.selectors";
import { Pageable } from "../../inventory/brands/BrandsResponse";
import { CustomersService } from "./customers.service";
import {
  CustomerEntity,
  CustomersResponse,
} from "./interfaces/CustomersResponse";
import { State } from "../../../state/reducers";

@Component({
  selector: "app-customers",
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    TranslatePipe,
    RouterLink,
    SearchInputComponent,
    NgClass,
    PagenationComponent,
  ],
  templateUrl: "./customers.component.html",
  styles: ``,
})
export class CustomersComponent {
  store: Store<State> = inject(Store<State>);
  selectLanguage$: Observable<LangState> = this.store.select(selectLanguage);
  activeRouter: ActivatedRoute = inject(ActivatedRoute);
  router: Router = inject(Router);
  customersService = inject(CustomersService);
  customers: CustomerEntity[] = [];
  customerResponse!: CustomersResponse;
  pageable!: Pageable;
  tableColumns: string[] = [
    "#",
    "Name",
    "Email",
    "Phone",
    "Address",
    "IsActive",
    // 'Actions'
  ];
  currentPage!: number;
  pageSize!: number;
  searchName: string = "";
  ngOnInit(): void {
    this.initPageParams();
  }
  initPageParams() {
    this.currentPage = this.activeRouter.snapshot.queryParams["page"] || 0;
    this.pageSize = this.activeRouter.snapshot.queryParams["size"] || 80;
    this.searchName = this.activeRouter.snapshot.queryParams["name"] || "";
    this.getData();
  }
  getData() {
    this.customersService
      .getCustomers(this.currentPage, this.pageSize, this.searchName)
      .subscribe((data) => {
        this.customerResponse = data;
        this.pageable = data.pageable;
        this.customers = data.content;
      });
  }
  generatePageArray() {
    return new Array(this.customerResponse?.totalPages);
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
  onSearchChanged($event: string) {
    this.searchName = $event;
    this.router.navigate([], {
      relativeTo: this.activeRouter,
      queryParams: { name: $event },
      queryParamsHandling: "merge",
    });
    this.getData();
  }
}
