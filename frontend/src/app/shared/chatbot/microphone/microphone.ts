import { Component, OnInit, inject } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { signal } from '@angular/core';

import { ChatReqDTO } from '../../../models/chatbot.model';
import { MicService } from '../../../services/mic-service';
import { ChatbotService } from '../../../services/chatbot-service';


@Component({
  selector: 'microphone',
  imports: [MatIconModule],
  templateUrl: './microphone.html'
})
export class Microphone implements OnInit {
  isMicOn = signal<boolean>(false);
  userTranscript: string = '';

  private micService = inject(MicService);
  private chatService = inject(ChatbotService);

  ngOnInit(): void {
    this.micService.init();
  }

  onToggleMic() {
    this.isMicOn.set(!this.isMicOn());

    if (this.isMicOn()) {
      this.micService.start();
    } else {
      this.micService.stop();
      this.userTranscript = this.micService.text;
      const userModel: ChatReqDTO = {
        username: 'Isaiah',
        message: this.userTranscript,
        level: 'B2'
      }

      this.chatService.generateChat(userModel).subscribe({
        next: (response) => console.log('Chat response:', response),
        error: (err) => console.error('Chat error:', err),
      });
    }

    console.warn(`Transcript : ${this.userTranscript}`)
  }
}
