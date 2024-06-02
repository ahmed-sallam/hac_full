import {Component, Input} from '@angular/core';
import {TranslatePipe} from "../../../pipes/translate.pipe";
import {DatePipe} from "@angular/common";
import {
  History
} from "../../purchases/material-request/interfaces/OneMaterialRequest";

@Component({
  selector: 'app-history-section',
  standalone: true,
  imports: [
    TranslatePipe,
    DatePipe
  ],
  templateUrl: './history-section.component.html',
  styles: ``
})
export class HistorySectionComponent {
  @Input() language!: string;
  @Input() history!: History[];

}
