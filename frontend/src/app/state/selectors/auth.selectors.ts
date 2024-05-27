import {State} from "../reducers";
import {createSelector} from "@ngrx/store";
import {AuthState} from "../reducers/auth.reducer";

export const selectAuth =(state: State)=>state.auth
export const selectToken = createSelector(
    selectAuth,
    (state: AuthState) => state.token
);
export const selectUser = createSelector(
    selectAuth,
    (state: AuthState) => state.user
);
