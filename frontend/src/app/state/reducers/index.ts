import {isDevMode} from '@angular/core';
import {ActionReducerMap, MetaReducer} from '@ngrx/store';
import {langReducer, LangState} from "./lang.reducer";

export interface State {
language: LangState
}

export const reducers: ActionReducerMap<State> = {
    language: langReducer
};


export const metaReducers: MetaReducer<State>[] = isDevMode() ? [] : [];
