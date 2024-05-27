import {CanActivate, Router} from '@angular/router';
import {Store} from "@ngrx/store";
import {AuthState} from "../state/reducers/auth.reducer";
import {map, Observable} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private store: Store<{ auth: AuthState }>, private router: Router) {}

  canActivate(): Observable<boolean> {
    return this.store.select('auth').pipe(
        map(authState => {
          if (authState.token) {
            return true;
          } else {
            this.router.navigate(['/login']);
            return false;
          }
        })
    );
  }
}