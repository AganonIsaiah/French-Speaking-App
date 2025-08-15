import { Component, signal } from '@angular/core';

import { TitleTemplate } from '../../shared/title-template/title-template';

@Component({
  selector: 'traduction',
  imports: [TitleTemplate],
  templateUrl: './traduction.html'
})
export class Traduction {
  translatedResponse = signal<string>('Testing');

} 
