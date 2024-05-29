import {Component, OnInit} from '@angular/core';
import {LoaderService} from "../../../components/loader/loader.service";
import {FilesService} from "../../../../files.service";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {selectLanguage} from "../../../../state/selectors/lang.selectors";
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {OneMaterialRequest} from "../interfaces/OneMaterialRequest";
import {MaterialRequestService} from "../material-request.service";
import {AsyncPipe, DatePipe} from "@angular/common";
import {
  MainContentComponent
} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";

@Component({
  selector: 'app-one-material-request',
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    TranslatePipe,
    RouterLink,
    DatePipe
  ],
  templateUrl: './one-material-request.component.html',
  styles: ``
})
export class OneMaterialRequestComponent implements OnInit{
  selectLanguage$!: Observable<LangState>;
  loader$!: Observable<boolean>;
  materialRequest!: OneMaterialRequest;
  requestId!: number;
  tableColumns: string[] = [
    '#',
    'Product',
    'Quantity',
    'Stock in store',
    'Total stock',
    'Notes',
  ];

  constructor(
      private loaderService: LoaderService,
      private filesService: FilesService,
      private store: Store<State>,
      private activeRouter: ActivatedRoute,
      private materialRequestService: MaterialRequestService,
      private router: Router,
  ) {
    this.loader$ = this.loaderService.isLoading;
    this.selectLanguage$ = this.store.select(selectLanguage);
  }

  ngOnInit(): void {
    this.initPageParams();
  }

  getData() {
    this.loaderService.show()

    this.materialRequestService.getOneMaterialRequest(this.requestId)
        .subscribe({
          next: (response: OneMaterialRequest) => {
            console.log(response)
            this.materialRequest = response;
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
