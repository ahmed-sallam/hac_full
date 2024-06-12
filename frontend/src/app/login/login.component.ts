import {Component, inject, OnInit} from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators
} from "@angular/forms";
import {TranslatePipe} from "../pipes/translate.pipe";
import {AsyncPipe, NgOptimizedImage} from "@angular/common";
import {Store} from "@ngrx/store";
import {State} from "../state/reducers";
import {selectLanguage} from "../state/selectors/lang.selectors";
import {login as loginAction} from "../state/actions/auth.actions"
import {selectToken} from "../state/selectors/auth.selectors";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, TranslatePipe, NgOptimizedImage, AsyncPipe, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styles: ``
})
export class LoginComponent implements OnInit{
  store = inject(Store<State>);
  router = inject(Router)
  selectLanguage$= this.store.select(selectLanguage)
  formGroup: FormGroup = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
    password: new FormControl('', [Validators.required, Validators.pattern(/^(\s+\S+\s*)*(?!\s).*$/)
    ]),
  });

  ngOnInit(): void {
    const token = this.store.select(selectToken)
    token.subscribe({next:(token)=>{
        this.router.navigate(["/dashboard"])
      }})
  }

  onSubmit() {
    this.formGroup.markAllAsTouched()
    if (this.formGroup.invalid) {
    }else{
      this.login(this.formGroup.value)
      }
  }
  login(loginData: LoginData){
    this.store.dispatch(loginAction(loginData))
  }


}

export interface LoginData{
  username: string;
  password: string;
}
