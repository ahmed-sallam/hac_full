import {Component, inject} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {TranslatePipe} from "../pipes/translate.pipe";
import {AsyncPipe, NgOptimizedImage} from "@angular/common";
import {Store} from "@ngrx/store";
import {State} from "../state/reducers";
import {selectLanguage} from "../state/selectors/lang.selectors";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, TranslatePipe, NgOptimizedImage, AsyncPipe],
  templateUrl: './login.component.html',
  styles: ``
})
export class LoginComponent {

  store= inject(Store<State>)
  selectLanguage$= this.store.select(selectLanguage)

  onSubmit() {
    console.log("Form Submit")
  }
}
