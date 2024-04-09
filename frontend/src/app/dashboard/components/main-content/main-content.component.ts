import {booleanAttribute, Component, Input} from '@angular/core';
import {TranslatePipe} from "../../../pipes/translate.pipe";
import {LangState} from "../../../state/reducers/lang.reducer";
import {EmptyDivComponent} from "../empty-div/empty-div.component";
import {LoaderComponent} from "../loader/loader.component";

@Component({
  selector: 'app-main-content',
  standalone: true,
  imports: [
    TranslatePipe,
    EmptyDivComponent,
    LoaderComponent
  ],
  templateUrl: './main-content.component.html',
  styles: ``
})
export class MainContentComponent {
  @Input() language!: LangState;
  @Input() headerText!: string;
    @Input() noDataMessage!: string | any;
    @Input() noData!: boolean;
  @Input({transform: booleanAttribute}) loader: boolean = false;

}
