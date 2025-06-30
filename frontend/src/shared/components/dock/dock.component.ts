import { Component } from '@angular/core';
import { MicrophoneService } from '../../../core/services/mic/microphone.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dock',
  imports: [],
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
