import { Component } from '@angular/core';
import { ChatMessage } from '../../models/chat-message';
import { ChatService } from '../../services/chat/chat.service';
import { AuthService } from '../../services/auth/auth.service';
import { FormsModule } from '@angular/forms';
import { NgFor, NgIf, NgClass } from '@angular/common';

@Component({
  selector: 'app-chat-input-response',
  imports: [FormsModule, NgFor, NgIf, NgClass],
  templateUrl: './chat-input-response.component.html',
  styleUrl: './chat-input-response.component.scss'
})
export class ChatInputResponseComponent {
  input: string = '';
  messages: ChatMessage[] = [];
  mode: 'conversation' | 'learning' = 'conversation';

  constructor(private chatService: ChatService, private authService: AuthService) { }

  sendMessage() {
    if (!this.input.trim()) return;

    const userLevel = this.authService.getUser()?.proficiency || 'A1';
    const username = this.authService.getUser().username;
   
    this.messages.push({
      sender: 'Vous',
      text: this.input
    });

    const getChatResponse = this.mode === 'conversation'
      ? this.chatService.conversation(this.input, userLevel, username)
      : this.chatService.learning(this.input, userLevel, username);

    getChatResponse.subscribe({
      next: (response) => {
        this.messages.push({ sender: 'IA', text: response});
        this.input = '';
      },
      error: () => {
        this.messages.push({ sender: 'IA', text: 'Une erreur s\'est produite' });
      }
     })
  }
}
