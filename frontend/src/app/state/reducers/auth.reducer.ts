import {createReducer, on} from '@ngrx/store';
import * as AuthActions from '../actions/auth.actions';

export interface AuthState {
    token: string | null;
    error: string | null;
    user?: any;
}

export const initialState: AuthState = {
    token: localStorage.getItem("token") || null, // todo move it to effects
    error: null,
    user: localStorage.getItem("user") || null // todo move it to effects
};

export const authReducer = createReducer(
    initialState,
    on(AuthActions.loginSuccess, (state, {token, user}) => ({
        ...state,
        token,
        user,
        error: null
    })),
    on(AuthActions.loginFailure, (state, {error}) => ({...state, error})),
    on(AuthActions.logout, (state) => ({
        ...state,
        token: null,
        error: null,
        user: null
    }))
);