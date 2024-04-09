import {Component, inject, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {State} from "../../../state/reducers";
import {selectLanguage} from "../../../state/selectors/lang.selectors";
import {AsyncPipe} from "@angular/common";
import {MainContentComponent} from "../../components/main-content/main-content.component";
import {RouterLink} from "@angular/router";
import {TranslatePipe} from "../../../pipes/translate.pipe";
import {SearchInputComponent} from "../../components/search-input/search-input.component";
import {CountriesService} from "./countries.service";
import {Country} from "./interfaces/Country";

@Component({
  selector: 'app-countries',
  standalone: true,
  imports: [
    AsyncPipe,
    MainContentComponent,
    RouterLink,
    TranslatePipe,
    SearchInputComponent
  ],
  templateUrl: './countries.component.html',
  styles: ``
})
export class CountriesComponent implements OnInit{
  store = inject(Store<State>);
  selectLanguage$ = this.store.select(selectLanguage);
  countryService: CountriesService = inject(CountriesService);
  countries: Country[] = [];
  dataBeforeSearch: Country[] = [];
    searchName: string='';

  ngOnInit(): void {
    this.getData();
  }
  onSearchChanged($event: string) {
    this.searchName = $event;
    this.countries = this.dataBeforeSearch.filter((country: Country) => {
      return country.nameAr.toLowerCase().includes($event.toLowerCase()) || country.nameEn.toLowerCase().includes($event.toLowerCase());
    });
  }

  getData(){
    this.countryService.getCountries().subscribe({
      next: (data: Country[]) => {
        this.countries = data;
        this.dataBeforeSearch = data;
      },
      error: (err) => {
        console.error(err);
      }
    });
  }
}
