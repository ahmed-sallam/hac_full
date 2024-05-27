import {createAction, props} from "@ngrx/store";
import {LoginData} from "../../login/login.component";

export const login = createAction(
    '[Auth] Login',
    props<LoginData>()
);

export const loginSuccess = createAction(
    '[Auth] Login Success',
    props<{ token: string, user: any }>()
);

export const loginFailure = createAction(
    '[Auth] Login Failure',
    props<{ error: any }>()
);

export const logout = createAction('[Auth] Logout');
export const initAuth = createAction('[Auth] Init');
