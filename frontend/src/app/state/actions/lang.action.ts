import {createAction, props} from "@ngrx/store";

export const changeLangAction = createAction(
    '[lang]',
    props<{lang:string, dir:string}>()
)