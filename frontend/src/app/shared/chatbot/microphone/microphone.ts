import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { signal } from '@angular/core';

@Component({
  selector: 'microphone',
  imports: [MatIconModule],
  templateUrl: './microphone.html'
})
export class Microphone {
  isMicOn = signal<boolean>(false);

  onToggleMic = () => this.isMicOn.set(!this.isMicOn());
}
