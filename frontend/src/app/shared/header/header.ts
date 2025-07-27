import { Component } from '@angular/core';
import {MatProgressBarModule} from '@angular/material/progress-bar';

@Component({
  selector: 'header',
  imports: [MatProgressBarModule],
  templateUrl: './header.html'
})
export class Header {
  username = 'isaiah';
  level = 'Niveau B2';
  points = 100;

}
