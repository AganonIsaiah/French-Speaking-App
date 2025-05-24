import { AfterViewChecked, Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ChatMessage } from '../../../core/models/chat-message';
import { ChatService } from '../../../core/services/chat/chat.service';
import { AuthService } from '../../../core/services/auth/auth.service';

import { FormsModule } from '@angular/forms';
import { NgFor, NgIf, NgClass } from '@angular/common';

@Component({
  selector: 'app-chat-input-response',
  imports: [FormsModule, NgFor, NgIf, NgClass],
  templateUrl: './chat-input-response.component.html',
  styleUrl: './chat-input-response.component.scss'
})
export class ChatInputResponseComponent implements AfterViewChecked {
  @ViewChild('scrollContainer') private scrollContainer!: ElementRef;

  input: string = '';
  messages: ChatMessage[] = [];
  mode: 'conversation' | 'learning' = 'conversation';

  constructor(private chatService: ChatService, private authService: AuthService) { }
  ngAfterViewChecked() {
    this.scrollToBottom();
  }

  scrollToBottom(): void {
    try {
      this.scrollContainer.nativeElement.scrollTop = this.scrollContainer.nativeElement.scrollHeight;
    } catch (err) {

    }
  }

  sendMessage() {
    if (!this.input.trim()) return;

    const userLevel = this.authService.getUser()?.proficiency || 'A1';
    const username = this.authService.getUser().username;

    console.log(`
      Username: ${username}
      Userlevel: ${userLevel}
      Mode: ${this.mode}
      `)
    this.messages.push({
      sender: 'Vous',
      text: this.input
    });



    const getChatResponse = this.mode === 'conversation'
      ? this.chatService.conversation(this.input, userLevel, username)
      : this.chatService.learning(this.input, userLevel, username);

    console.log("Sending message:", { message: this.input, level: userLevel, username });

    getChatResponse.subscribe({
      next: (response) => {
        this.messages.push({ sender: 'IA', text: response });
      },
      error: () => {
        this.messages.push({ sender: 'IA', text: 'Une erreur s\'est produite' });
      }
    })
    this.input = '';
  }
}
