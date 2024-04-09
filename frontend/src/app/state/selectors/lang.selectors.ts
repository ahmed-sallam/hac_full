import {State} from "../reducers";
import {createSelector} from "@ngrx/store";
import {LangState} from "../reducers/lang.reducer";

export const selectLanguage =(state: State)=>state.language
export const selectLang = createSelector(
    selectLanguage,
    (state: LangState) => state.lang
);
export const selectDir = createSelector(
    selectLanguage,
    (state: LangState) => state.dir
);
