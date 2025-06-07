import { Component } from '@angular/core';
import { MicrophoneService } from '../../../core/services/mic/microphone.service';
import { AsyncPipe } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dock',
  imports: [AsyncPipe],
  templateUrl: './dock.component.html',
  styleUrl: './dock.component.scss'
})
export class DockComponent {
  constructor(
    public micService: MicrophoneService,
    private router: Router) { }

  onMic() {
    this.micService.toggle();
  }

  onQuit() {
    this.router.navigate(['/home']);
  }
}
