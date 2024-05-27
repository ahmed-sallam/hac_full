import {
    HttpEvent,
    HttpHandler,
    HttpInterceptor,
    HttpRequest
} from '@angular/common/http';
import {inject, Injectable} from "@angular/core";
import {Observable, switchMap} from "rxjs";
import {AuthState} from "./state/reducers/auth.reducer";
import {Store} from "@ngrx/store";
import {selectToken} from "./state/selectors/auth.selectors";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    store = inject(Store<AuthState>)
    token$ = this.store.select(selectToken)

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return this.token$.pipe(switchMap((authToken) => {
            if (request.url.includes('/login')) {
                return next.handle(request);
            }
            const authReq = request.clone({
                headers: request.headers.set('Authorization', `Bearer ${authToken}`)
            });
            return next.handle(authReq);
        }));


    }
}