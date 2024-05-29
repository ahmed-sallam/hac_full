import {Component, inject, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../state/reducers";
import {Observable} from "rxjs";
import {LangState} from "../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../state/selectors/lang.selectors";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {BrandsService} from "./brands.service";
import {BrandEntity, BrandsResponse} from "./BrandsResponse";
import {AsyncPipe, DatePipe, NgClass} from "@angular/common";
import {
  MainContentComponent
} from "../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../pipes/translate.pipe";
import {
  SearchInputComponent
} from "../../components/search-input/search-input.component";
import {Pageable} from "../stores/interfaces/StoreResponse";

@Component({
  selector: 'app-brands',
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    TranslatePipe,
    RouterLink,
    SearchInputComponent,
    DatePipe,
    NgClass
  ],
  templateUrl: './brands.component.html',
  styles: ``
})
export class BrandsComponent implements OnInit{
  store: Store<State> = inject(Store<State>);
  selectLanguage$: Observable<LangState> = this.store.select(selectLanguage);
  activeRouter: ActivatedRoute = inject(ActivatedRoute);
  router: Router = inject(Router);
  brandService: BrandsService = inject(BrandsService);
  brands: BrandEntity[]= [];
  brandsResponse!: BrandsResponse;
  pageable!: Pageable;

  tableColumns: string[] = [
    '#',
    'Name',
    'Last updated',
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
    return new Array(this.brandsResponse?.totalPages);
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
    this.activeRouter.queryParams.subscribe((params) => {
      this.searchName = params['name'] || '';
      this.currentPage = params['page'] || 0;
      this.pageSize = params['size'] || 80;
      this.getData();

    });
  }

  private getData() {
    this.brandService
        .getBrands(this.currentPage, this.pageSize, this.searchName)
        .subscribe((data) => {
          this.brandsResponse = data;
          this.pageable = data.pageable;
          this.brands = data.content;
        });
  }
}
