import {createReducer, on} from "@ngrx/store";
import {changeLangAction} from "../actions/lang.action";

export interface LangState {
    lang: string;
    dir: string;
}
export const initLangState:LangState= {
    lang:"en",
    dir:"ltr"
}
export const langReducer = createReducer(
    initLangState,
    on(changeLangAction, (state:LangState, {lang, dir}) => {
        return {
            lang, dir
        }
    })
)