import { Component } from "@angular/core";
import { MicrophoneService } from "../../../core/services/mic/microphone.service";
import { AsyncPipe } from "@angular/common";

@Component({
  selector: 'app-microphone',
  imports: [AsyncPipe],
  templateUrl: './microphone.component.html',
  styleUrl: './microphone.component.scss'
})
export class MicrophoneComponent  {
  constructor(public micService: MicrophoneService) {}

  onToggle() {
    this.micService.toggle();
  }
}