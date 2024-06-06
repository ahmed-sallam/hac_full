import {ApplicationConfig, isDevMode} from '@angular/core';
import {metaReducers, reducers} from './state/reducers';

import {LangEffects} from './state/effects/lang.effect';
import {provideEffects} from '@ngrx/effects';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import {provideRouter} from '@angular/router';
import {provideStore} from '@ngrx/store';
import {provideStoreDevtools} from '@ngrx/store-devtools';
import {routes} from './app.routes';
import {AuthEffects} from "./state/effects/auth.effects";
import {AuthInterceptor} from "./auth.interceptor";
import { provideServiceWorker } from '@angular/service-worker';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideStore(reducers, { metaReducers }),
    provideEffects([LangEffects, AuthEffects]),
    provideStoreDevtools({ maxAge: 25, logOnly: !isDevMode() }),
    provideHttpClient(withInterceptorsFromDi()),
    {
        provide: HTTP_INTERCEPTORS,
        useClass: AuthInterceptor,
        multi: true,
    },
    provideServiceWorker('ngsw-worker.js', {
        enabled: !isDevMode(),
        registrationStrategy: 'registerWhenStable:30000'
    })
],
};
