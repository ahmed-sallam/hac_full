import {Component, inject, Input} from '@angular/core';
import {AsyncPipe, NgIf, NgOptimizedImage} from "@angular/common";
import {DashboardService} from "../../dashboard.service";
import {Store} from "@ngrx/store";
import {State} from "../../../state/reducers";
import {changeLangAction} from "../../../state/actions/lang.action";
import {LangState} from "../../../state/reducers/lang.reducer";
import {selectUser} from "../../../state/selectors/auth.selectors";
import {Observable} from "rxjs";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    NgOptimizedImage,
    AsyncPipe,
    NgIf
  ],
  templateUrl: './navbar.component.html',
  styles: ``
})
export class NavbarComponent {
  dashboardService = inject(DashboardService)
  store= inject(Store<State>)
  showSidebar$ = this.dashboardService.sidebarVisibility;
  @Input() language!: LangState;

  user$: Observable<any> = this.store.select(selectUser);
  changeLanguage(lang:string, dir:string){
    this.store.dispatch(changeLangAction({lang, dir}));
  }


}
