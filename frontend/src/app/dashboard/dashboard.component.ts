import {Component, inject} from '@angular/core';
import {AsyncPipe, NgOptimizedImage} from "@angular/common";
import {NavbarComponent} from "./components/navbar/navbar.component";
import {SidebarComponent} from "./components/sidebar/sidebar.component";
import {RouterOutlet} from "@angular/router";
import {Store} from "@ngrx/store";
import {State} from "../state/reducers";
import {selectLanguage} from "../state/selectors/lang.selectors";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    NgOptimizedImage,
    NavbarComponent,
    SidebarComponent,
    RouterOutlet,
    AsyncPipe
  ],
  templateUrl: './dashboard.component.html',
  styles: ``
})
export class DashboardComponent {
  store= inject(Store<State>)
  selectLanguage$= this.store.select(selectLanguage)
}
