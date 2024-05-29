import {Component, inject} from '@angular/core';
import {TranslatePipe} from "../../../pipes/translate.pipe";
import {
  MainContentComponent
} from "../../components/main-content/main-content.component";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {
  SearchInputComponent
} from "../../components/search-input/search-input.component";
import {AsyncPipe, DatePipe, NgClass} from "@angular/common";
import {Store} from "@ngrx/store";
import {State} from "../../../state/reducers";
import {Observable} from "rxjs";
import {LangState} from "../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../state/selectors/lang.selectors";
import {MachineryService} from "./machinery.service";
import {
  MachineryResponse,
  MachineryTypeEntity
} from "./interfaces/MachineryResponse";
import {Pageable} from "../brands/BrandsResponse";

@Component({
  selector: 'app-machinery',
  standalone: true,
  imports: [
    TranslatePipe,
    MainContentComponent,
    RouterLink,
    SearchInputComponent,
    DatePipe,
    NgClass,
    AsyncPipe
  ],
  templateUrl: './machinery.component.html',
  styles: ``
})
export class MachineryComponent {
  store: Store<State> = inject(Store<State>);
  selectLanguage$: Observable<LangState> = this.store.select(selectLanguage);
  activeRouter: ActivatedRoute = inject(ActivatedRoute);
  router: Router = inject(Router);
  machineryService: MachineryService = inject(MachineryService);
  machinery : MachineryTypeEntity [] = [];
  machineryResponse !: MachineryResponse;
  pageable !: Pageable;

  tableColumns: string[] = [
    '#',
    'Name',
      'Is Active',
      'Models Count',
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
    return new Array(this.machineryResponse?.totalPages);
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
    this.machineryService
        .getMachinery(this.currentPage, this.pageSize, this.searchName)
        .subscribe((data) => {
          this.machineryResponse = data;
          this.pageable = data.pageable;
          this.machinery = data.content;
        });
  }
}
