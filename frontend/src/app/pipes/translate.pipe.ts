import { Pipe, PipeTransform } from '@angular/core';
import * as data from '../locals.json';

@Pipe({
  name: 'translate',
  standalone: true
})
export class TranslatePipe implements PipeTransform {

  transform(value: any, lang: any, args?: any): any {
    if (lang == 'ar') return (<any>data).default[value.trim()];
    else return value;
  }

}
