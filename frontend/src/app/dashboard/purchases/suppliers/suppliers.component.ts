import {Component, inject} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../state/reducers";
import {Observable} from "rxjs";
import {LangState} from "../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../state/selectors/lang.selectors";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {SuppliersService} from "./suppliers.service";
import {
  SupplierEntity,
  SuppliersResponse
} from "./interfaces/SuppliersResponse";
import {Pageable} from "../../inventory/stores/interfaces/StoreResponse";
import {AsyncPipe, NgClass} from "@angular/common";
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
  selector: 'app-suppliers',
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    TranslatePipe,
    RouterLink,
    SearchInputComponent,
    NgClass,
    PagenationComponent
  ],
  templateUrl: './suppliers.component.html',
  styles: ``
})
export class SuppliersComponent {
  store: Store<State> = inject(Store<State>);
  selectLanguage$: Observable<LangState> = this.store.select(selectLanguage);
  activeRouter: ActivatedRoute = inject(ActivatedRoute);
  router: Router = inject(Router);
  suppliersService = inject(SuppliersService);
  suppliers:SupplierEntity[] = [];
  supplierResponse!: SuppliersResponse;
  pageable!: Pageable;
  tableColumns: string[] = [
    '#',
    'Name',
    'Email',
    'Phone',
    'Address',
    'IsActive',
    // 'Actions'
  ];
  currentPage!: number;
  pageSize!: number;
  searchName: string = '';
  ngOnInit(): void {
    this.initPageParams();
  }
  onSearchChanged($event: string) {
    this.searchName = $event;
    this.router.navigate([], {
      relativeTo: this.activeRouter,
      queryParams: { name: $event },
      queryParamsHandling: 'merge',
    });
    this.getData();
  }
  generatePageArray() {
    return new Array(this.supplierResponse?.totalPages);
  }
  onPageChanged($index: number) {
    if($index==-1){
      return
    }
    if(+this.currentPage == $index){
      return
    }
    this.currentPage = $index;
    this.router.navigate([], {
      relativeTo: this.activeRouter,
      queryParams: { page: $index },
      queryParamsHandling: 'merge',
    });
    this.getData();
  }
  private initPageParams() {
    this.currentPage = this.activeRouter.snapshot.queryParams['page'] || 0;
    this.pageSize = this.activeRouter.snapshot.queryParams['size'] || 80;
    this.searchName = this.activeRouter.snapshot.queryParams['name'] || '';
    this.getData();
  }

  private getData() {
    this.suppliersService
        .getSubbliers(this.currentPage, this.pageSize, this.searchName)
        .subscribe((data) => {
          this.supplierResponse = data;
          this.pageable = data.pageable;
          this.suppliers = data.content;
        });
  }
}
