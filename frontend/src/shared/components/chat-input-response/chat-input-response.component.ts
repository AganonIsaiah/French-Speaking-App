import { Component, OnInit } from '@angular/core';
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
export class ChatInputResponseComponent implements OnInit {
  input: string = '';
  messages: ChatMessage[] = [];
  mode: 'conversation' | 'learning' = 'conversation';

  constructor(private chatService: ChatService, private authService: AuthService) { }

  ngOnInit(){
    this.dummyValues();
  }

  dummyValues() {
    this.messages.push({ sender: 'Vous', text: 'Bonjour!' })
    this.messages.push({ sender: 'IA', text: 'Lorem ipsum dolor sit amet consectetur adipisicing elit. Eum ab dolor alias similique quasi cumque debitis mollitia doloribus, ad necessitatibus facere placeat recusandae esse deserunt? Error, minus id. Rerum, distinctio!' })
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

    this.input = '';

    const getChatResponse = this.mode === 'conversation'
      ? this.chatService.conversation(this.input, userLevel, username)
      : this.chatService.learning(this.input, userLevel, username);

     
    getChatResponse.subscribe({
      next: (response) => {
        this.messages.push({ sender: 'IA', text: response});
      },
      error: () => {
        this.messages.push({ sender: 'IA', text: 'Une erreur s\'est produite' });
      }
     })
  }
}
