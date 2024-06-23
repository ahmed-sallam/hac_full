import {Component, inject, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../state/reducers";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {Observable} from "rxjs";
import {LangState} from "../../../state/reducers/lang.reducer";
import {selectLanguage} from "../../../state/selectors/lang.selectors";
import {selectUser} from "../../../state/selectors/auth.selectors";
import {Pageable} from "../../inventory/stores/interfaces/StoreResponse";
import {AsyncPipe} from "@angular/common";
import {
  MainContentComponent
} from "../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../pipes/translate.pipe";

@Component({
  selector: 'app-bid-summary',
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    TranslatePipe,
    RouterLink
  ],
  templateUrl: './bid-summary.component.html',
  styles: ``
})
export class BidSummaryComponent implements OnInit{

  store: Store<State> = inject(Store<State>);
  activeRouter: ActivatedRoute = inject(ActivatedRoute);
  router: Router = inject(Router);
  selectLanguage$: Observable<LangState> = this.store.select(selectLanguage);
  selectUser$: Observable<any> = this.store.select(selectUser);
  pageable!: Pageable;
  tableColumns: string[] = [
    'Id',
  ];
  currentPage!: number;
  pageSize!: number;
  searchName: string = '';
  sort!: string|undefined;

  bidSummaries=[];
  ngOnInit(): void {
    // this.initPageParams();
    // this.getData();
  }
}
