import {inject, Injectable} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {changeLangAction} from "../actions/lang.action";
import {defer, of, tap} from "rxjs";

@Injectable()
export class LangEffects {
    actions$ = inject(Actions)
    changeLange$ = createEffect(
        () =>
            this.actions$.pipe(
                ofType(changeLangAction),
                tap((action) => {
                    localStorage.setItem('HACLang', JSON.stringify(action.lang))
                    localStorage.setItem('HACDir', JSON.stringify(action.dir))
                })
            ),
        {dispatch: false}
    )

    initLang$ = createEffect(() =>
        defer(() => {
            const langItem = localStorage.getItem('HACLang')
            const dirItem = localStorage.getItem('HACDir')
            return langItem && dirItem
                ? of(changeLangAction({lang: JSON.parse(langItem), dir: JSON.parse(dirItem)}))
                : of(
                    changeLangAction({
                        lang: 'en',
                        dir: 'ltr'
                    })
                )

        })
    )

}