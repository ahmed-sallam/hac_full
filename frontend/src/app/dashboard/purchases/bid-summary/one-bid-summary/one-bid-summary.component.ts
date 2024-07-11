import {Component, inject, OnInit} from '@angular/core';
import {Observable} from "rxjs";
import {LangState} from "../../../../state/reducers/lang.reducer";
import {Store} from "@ngrx/store";
import {State} from "../../../../state/reducers";
import {AsyncPipe} from "@angular/common";
import {
  MainContentComponent
} from "../../../components/main-content/main-content.component";
import {TranslatePipe} from "../../../../pipes/translate.pipe";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-one-bid-summary',
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    TranslatePipe,
    RouterLink
  ],
  templateUrl: './one-bid-summary.component.html',
  styles: ``
})
export class OneBidSummaryComponent implements OnInit{
  selectLanguage$!: Observable<LangState>;
  loader$!: Observable<boolean>;
  store: Store<State> = inject(Store<State>)


  ngOnInit(): void {
    // this.initPageParams();
  }

}
