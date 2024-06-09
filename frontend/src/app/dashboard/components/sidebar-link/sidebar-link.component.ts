import {Component, inject, Input, OnInit} from '@angular/core';
import {
    NavigationEnd,
    Router,
    RouterLink,
    RouterLinkActive
} from "@angular/router";
import {LangState} from "../../../state/reducers/lang.reducer";
import {TranslatePipe} from "../../../pipes/translate.pipe";
import {NgClass} from "@angular/common";

@Component({
    selector: 'app-sidebar-link',
    standalone: true,
    imports: [
        RouterLink,
        RouterLinkActive,
        TranslatePipe,
        NgClass
    ],
    templateUrl: './sidebar-link.component.html',
    styles: ``
})
export class SidebarLinkComponent implements OnInit {
    @Input() linkText!: string;
    @Input() icon!: string;
    @Input() path!: string;
    @Input() nestedLinks!: any[] | { path: string; icon: string; text: string }[];
    @Input() language!: LangState;
    openNestMenu: boolean = false;
    router = inject(Router)

    ngOnInit(): void {
        const url = this.router.url;
        if (url.includes(this.path)) {
            this.openNestMenu = true;
        }
        this.router.events.subscribe((event) => {
                if (
                    event instanceof NavigationEnd
                ) {
                    const url = this.router.url;
                    if (url.includes(this.path)) {
                        this.openNestMenu = true;
                    }
                }

            }
        )
    }

    toggleNestedMenu() {
        this.openNestMenu = !this.openNestMenu;
    }
}
