import {Component, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {LoaderService} from "../../../components/loader/loader.service";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {OneRFPQ} from "../interfaces/OneRFPQ";
import {RfpqService} from "../rfpq.service";
import {AsyncPipe, DatePipe} from "@angular/common";
import {
  MainContentComponent
} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";

@Component({
  selector: 'app-one-rfpq',
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    TranslatePipe,
    RouterLink,
    DatePipe
  ],
  templateUrl: './one-rfpq.component.html',
  styles: ``
})
export class OneRfpqComponent implements OnInit{
  selectLanguage$!: Observable<LangState>;
  loader$!: Observable<boolean>;
  rfpq!: OneRFPQ;
  requestId!: number;
  tableColumns: string[] = [
    '#',
    'Product',
    'Quantity',
    'Notes',
  ];

  constructor(
      private loaderService: LoaderService,
      private store: Store<State>,
      private activeRouter: ActivatedRoute,
      private rfpqService: RfpqService,
  ) {
    this.loader$ = this.loaderService.isLoading;
    this.selectLanguage$ = this.store.select(selectLanguage);
  }

  ngOnInit(): void {
    this.initPageParams();
  }

  getData() {
    this.loaderService.show()

    this.rfpqService.getOneRFPQ(this.requestId)
        .subscribe({
          next: (response: OneRFPQ) => {
            this.rfpq = response;
            this.loaderService.hide()
          }
        });
  }

  private initPageParams() {
    this.loaderService.show()
    this.activeRouter.params.subscribe(params => {
      this.requestId = params['id'];
      this.requestId && this.getData()
    })
  }
}
