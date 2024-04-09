import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-empty-div',
  standalone: true,
  imports: [],
  templateUrl: './empty-div.component.html',
  styles: ``
})
export class EmptyDivComponent {
  @Input() noDataMessage: string = '';

}
