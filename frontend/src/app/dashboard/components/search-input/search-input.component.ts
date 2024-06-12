import {Component, EventEmitter, Input, Output} from '@angular/core';

import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-search-input',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './search-input.component.html',
  styles: ``,
})
export class SearchInputComponent {
  @Input() placeholder!: string;
  @Input() value: string = '';
  @Output() searchChanged: EventEmitter<string> = new EventEmitter();
  binding: any;

  onChange($event: any) {
    setTimeout(() => {
      this.searchChanged.emit($event.target.value);
      // this.value=$event.target.value
    }, 700);
  }

  reset() {
    this.searchChanged.emit('');
    this.value=''
    }
}
