import {isDevMode} from '@angular/core';
import {ActionReducerMap, MetaReducer} from '@ngrx/store';
import {langReducer, LangState} from "./lang.reducer";
import {authReducer, AuthState} from "./auth.reducer";

export interface State {
language: LangState,
    auth: AuthState
}

export const reducers: ActionReducerMap<State> = {
    language: langReducer,
    auth: authReducer
};


export const metaReducers: MetaReducer<State>[] = isDevMode() ? [] : [];
