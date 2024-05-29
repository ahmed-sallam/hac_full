import {Component, inject, OnInit} from '@angular/core';
import {TranslatePipe} from '../../../pipes/translate.pipe';
import {Store} from '@ngrx/store';
import {State} from '../../../state/reducers';
import {selectLanguage} from '../../../state/selectors/lang.selectors';
import {AsyncPipe, DatePipe, NgClass} from '@angular/common';
import {
    MainContentComponent
} from '../../components/main-content/main-content.component';
import {
    SearchInputComponent
} from '../../components/search-input/search-input.component';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {MachinePartsService} from "./machine-parts.service";
import {Observable} from "rxjs";
import {LangState} from "../../../state/reducers/lang.reducer";
import {
    MachinePartEntity,
    MachinePartResponse
} from "./interfaces/MachinePartResponse";
import {Pageable} from "../stores/interfaces/StoreResponse";

@Component({
    selector: 'app-machine-parts',
    standalone: true,
    imports: [
        TranslatePipe,
        AsyncPipe,
        MainContentComponent,
        SearchInputComponent,
        RouterLink,
        DatePipe,
        NgClass,
    ],
    templateUrl: './machine-parts.component.html',
    styles: ``,
})
export class MachinePartsComponent implements  OnInit{
    store: Store<State> = inject(Store<State>);
    selectLanguage$: Observable<LangState> = this.store.select(selectLanguage);
    activeRouter: ActivatedRoute = inject(ActivatedRoute);
    router: Router = inject(Router);
    machinePartsService: MachinePartsService = inject(MachinePartsService);
    machineParts: MachinePartEntity[]= [];
    machinePartResponse!: MachinePartResponse;
    pageable!: Pageable;

    tableColumns: string[] = [
        '#',
        'Name',
        'Last updated',
        // 'Actions'
    ];

    currentPage!: number;
    pageSize!: number;
    searchName: string = '';
    ngOnInit(): void {
        this.initPageParams();
        this.getData();
    }

    onSearchChanged($event: string) {
        this.searchName = $event;
        this.router.navigate([], {
            relativeTo: this.activeRouter,
            queryParams: { name: $event },
            queryParamsHandling: 'merge',
        });
        this.getData();
    }

    generatePageArray() {
        return new Array(this.machinePartResponse?.totalPages);
    }

    onPageChanged($index: number) {
        if($index==-1){
            return
        }
        if(+this.currentPage == $index){
            return
        }
        this.currentPage = $index;
        this.router.navigate([], {
            relativeTo: this.activeRouter,
            queryParams: { page: $index },
            queryParamsHandling: 'merge',
        });
        this.getData();
    }

    private initPageParams() {
        this.activeRouter.queryParams.subscribe((params) => {
            this.searchName = params['name'] || '';
            this.currentPage = params['page'] || 0;
            this.pageSize = params['size'] || 80;
        });
    }

    private getData() {
        this.machinePartsService
            .getMachineParts(this.currentPage, this.pageSize, this.searchName)
            .subscribe((data) => {
                this.machinePartResponse = data;
                this.pageable = data.pageable;
                this.machineParts = data.content;
            });
    }


}
