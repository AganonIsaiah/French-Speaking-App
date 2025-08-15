import { Component, OnInit, inject } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { signal } from '@angular/core';

import { ChatReqDTO } from '../../../models/chatbot.model';
import { STTService } from '../../../services/stt-service';
import { ChatbotService } from '../../../services/chatbot-service';


@Component({
  selector: 'microphone',
  imports: [MatIconModule],
  templateUrl: './microphone.html'
})
export class Microphone implements OnInit {
  isMicOn = signal<boolean>(false);
  userTranscript: string = '';

  private sttService = inject(STTService);
  private chatService = inject(ChatbotService);

  ngOnInit(): void {
    this.sttService.init();
  }

  onToggleMic() {
    this.isMicOn.set(!this.isMicOn());

    if (this.isMicOn()) {
      this.sttService.start();
    } else {
      this.sttService.stop();
      this.userTranscript = this.sttService.text;

      const userString = localStorage.getItem('user');
      const user = userString ? JSON.parse(userString) : { username: 'Guest', level: 'A1' };

      const userModel: ChatReqDTO = {
        username: user.username || 'Guest',
        message: this.userTranscript,
        level: user.level || 'A1'
      };

      this.chatService.generateChat(userModel).subscribe({
        next: (response) => console.log('Chat response:', response),
        error: (err) => console.error('Chat error:', err),
      });

      this.sttService.text = '';
    }

    console.warn(`Transcript : ${this.userTranscript}`)
  }
}
