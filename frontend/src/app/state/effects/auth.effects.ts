import {Injectable} from '@angular/core';
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {AuthService} from "../../login/auth.service";
import * as AuthActions from "../actions/auth.actions"
import {catchError, map, mergeMap, of, tap} from "rxjs";

@Injectable()
export class AuthEffects {
    login$ = createEffect(() =>
        this.actions$.pipe(
            ofType(AuthActions.login),
            mergeMap(action =>
                this.authService.login(action.username, action.password).pipe(
                    map(data => AuthActions.loginSuccess({
                        token: data.accessToken,
                        user: data.user
                    })),
                    catchError(error => of(AuthActions.loginFailure({error})))
                )
            )
        )
    );
    loginSuccess$ = createEffect(() =>
            this.actions$.pipe(
                ofType(AuthActions.loginSuccess),
                tap(action => {
                    localStorage.setItem('token', action.token);
                    localStorage.setItem('user', JSON.stringify(action.user));
                })
            ),
        {dispatch: false}
    );
    logout$ = createEffect(() =>
            this.actions$.pipe(
                ofType(AuthActions.logout),
                tap(() => {
                    localStorage.removeItem('token');
                    localStorage.removeItem('user');
                })
            ),
        {dispatch: false}
    );
    initAuth$ = createEffect(() =>
        this.actions$.pipe(
            ofType(AuthActions.initAuth),
            map(() => {
                const token : string | null = localStorage.getItem('token');
                const userStr : string | null = localStorage.getItem('user');
                if (token && userStr) {
                const user:any = JSON.parse(userStr);
                    return AuthActions.loginSuccess({token, user});
                } else {
                    return {type: '[Auth] No Action'};
                }
            })
        )
    );

    constructor(
        private actions$: Actions,
        private authService: AuthService
    ) {
    }
}