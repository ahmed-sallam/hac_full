import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [],
  templateUrl: './toast.component.html',
  styles: ``
})
export class ToastComponent {
  @Input() sccess: boolean |any = false;
  @Input() error: boolean |any= false;
  @Input() warning: boolean |any= false;
}
