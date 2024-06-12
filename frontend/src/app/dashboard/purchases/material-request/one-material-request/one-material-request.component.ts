import {Component, OnInit} from '@angular/core';
import {LoaderService} from "../../../components/loader/loader.service";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {ActivatedRoute, RouterLink} from "@angular/router";
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
import {
  HistorySectionComponent
} from "../../../components/history-section/history-section.component";

@Component({
  selector: 'app-one-material-request',
  standalone: true,
    imports: [
        AsyncPipe,
        MainContentComponent,
        TranslatePipe,
        RouterLink,
        DatePipe,
        HistorySectionComponent
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
      private store: Store<State>,
      private activeRouter: ActivatedRoute,
      private materialRequestService: MaterialRequestService,
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
            this.materialRequest = response;
            this.loaderService.hide()
            }
        });
  }

  updateMaterialRequestStatus(status: string){
    this.materialRequestService.updateMaterialRequestStatus(this.requestId, status).subscribe({
      next:()=>{this.getData()},
      error:(e)=>{
        }
    })
  }

  private initPageParams() {
    this.loaderService.show()
    this.activeRouter.params.subscribe(params => {
      this.requestId = params['id'];
      this.requestId && this.getData()
    })
  }
}
