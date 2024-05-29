import {Component, inject, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {selectLanguage} from '../../../state/selectors/lang.selectors';
import {State} from '../../../state/reducers';
import {
  SearchInputComponent
} from '../../components/search-input/search-input.component';
import {
  MainContentComponent
} from '../../components/main-content/main-content.component';
import {AsyncPipe, DatePipe, NgClass} from '@angular/common';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {TranslatePipe} from '../../../pipes/translate.pipe';
import {StoresService} from './stores.service';
import {
  Pageable,
  StoreEntity as StoreEntity,
  StoreResponse,
} from './interfaces/StoreResponse';

@Component({
  selector: 'app-stores',
  standalone: true,
  templateUrl: './stores.component.html',
  styles: ``,
  imports: [
    TranslatePipe,
    DatePipe,
    AsyncPipe,
    MainContentComponent,
    SearchInputComponent,
    RouterLink,
    NgClass,
  ],
})
export class StoresComponent implements OnInit {
  store = inject(Store<State>);
  activeRouter = inject(ActivatedRoute);
  router = inject(Router);
  storeService = inject(StoresService);
  selectLanguage$ = this.store.select(selectLanguage);

  stores: StoreEntity[] = [];
  storeResponse!: StoreResponse;
  pageable!: Pageable;

  tableColumns: string[] = [
    'Name',
    'City',
    'Status',
    'Last updated',
    'Location count',
    // 'Actions',
  ];
  currentPage!: number;
  pageSize!: number;
  searchName: string = '';
  ngOnInit(): void {
    this.initPageParams();
    this.getData();
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
    return new Array(this.storeResponse.totalPages);
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
    });
  }

  private getData() {
    this.storeService
      .getStores(this.currentPage, this.pageSize, this.searchName)
      .subscribe((data) => {
        this.storeResponse = data;
        this.pageable = data.pageable;
        this.stores = data.content;
      });
  }
}
