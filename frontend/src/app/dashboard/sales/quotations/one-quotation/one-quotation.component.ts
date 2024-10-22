import { ActivatedRoute, RouterLink } from "@angular/router";
import { AsyncPipe, DatePipe } from "@angular/common";

import { Component } from "@angular/core";
import { HistorySectionComponent } from "../../../components/history-section/history-section.component";
import { LangState } from "../../../../state/reducers/lang.reducer";
import { LoaderService } from "../../../components/loader/loader.service";
import { MainContentComponent } from "../../../components/main-content/main-content.component";
import { Observable } from "rxjs";
import { OneQuotation } from "../interfaces/OneQuotation";
import { QuotationsService } from "../quotations.service";
import { State } from "../../../../state/reducers";
import { Store } from "@ngrx/store";
import { TranslatePipe } from "../../../../pipes/translate.pipe";
import { selectLanguage } from "../../../../state/selectors/lang.selectors";

@Component({
  selector: "app-one-quotation",
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    TranslatePipe,
    RouterLink,
    DatePipe,
    HistorySectionComponent,
  ],
  templateUrl: "./one-quotation.component.html",
  styles: ``,
})
export class OneQuotationComponent {
  selectLanguage$!: Observable<LangState>;
  loader$!: Observable<boolean>;
  quotation!: OneQuotation;
  quotationId!: number;
  tableColumns: string[] = [
    "#",
    "Product",
    "Quantity",
    "price",
    "Discount (%)",
    "Total",
    "Notes",
  ];

  constructor(
    private loaderService: LoaderService,
    private store: Store<State>,
    private activeRouter: ActivatedRoute,
    private quotationsService: QuotationsService
  ) {
    this.loader$ = this.loaderService.isLoading;
    this.selectLanguage$ = this.store.select(selectLanguage);
  }

  ngOnInit(): void {
    this.initPageParams();
  }

  getData() {
    this.loaderService.show();

    this.quotationsService.getOneQuotation(this.quotationId).subscribe({
      next: (response: OneQuotation) => {
        this.quotation = response;
        this.loaderService.hide();
      },
    });
  }

  private initPageParams() {
    this.loaderService.show();
    this.activeRouter.params.subscribe((params) => {
      this.quotationId = params["id"];
      this.quotationId && this.getData();
    });
  }

  updateQuotationStatus(status: string) {
    this.quotationsService
      .updateQuotationStatus(this.quotationId, status)
      .subscribe({
        next: () => {
          this.getData();
        },
      });
  }
}
