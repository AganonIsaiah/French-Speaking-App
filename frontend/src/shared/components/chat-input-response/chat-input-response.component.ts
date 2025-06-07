import { AfterViewChecked, Component, OnInit, ViewChild, ElementRef, OnDestroy } from '@angular/core';
import { ChatMessage } from '../../../core/models/chat-message';
import { ChatService } from '../../../core/services/chat/chat.service';
import { AuthService } from '../../../core/services/auth/auth.service';
import { MicrophoneService } from '../../../core/services/mic/microphone.service';
import { TtsService } from '../../../core/services/tts/tts.service';

import { Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { NgFor, NgIf, NgClass } from '@angular/common';

@Component({
  selector: 'app-chat-input-response',
  imports: [FormsModule, NgFor, NgIf, NgClass],
  templateUrl: './chat-input-response.component.html',
  styleUrl: './chat-input-response.component.scss'
})
export class ChatInputResponseComponent implements AfterViewChecked, OnInit, OnDestroy {
  @ViewChild('scrollContainer') private scrollContainer!: ElementRef;

  input: string = '';
  messages: ChatMessage[] = [];
  mode: 'conversation' | 'learning' = 'conversation';
  private micSub?: Subscription;

  constructor(
    private chatService: ChatService,
    private authService: AuthService,
    private micService: MicrophoneService,
    private ttsService: TtsService
  ) {   
  }


  ngOnInit(): void {
    this.micSub = this.micService.currentTranscript$.subscribe((transcript) => {
      if (transcript.trim()) {
        this.messages.push({ sender: 'Vous', text: transcript });
      }
    });

    this.micService.chatResponse$.subscribe((response) => {
      if (response.trim()) {
        this.ttsService.speak(response);
        this.messages.push({ sender: 'IA', text: response });
      }
    });
  }

  ngAfterViewChecked() {
    this.scrollToBottom();
  }

  ngOnDestroy(): void {
    this.micSub?.unsubscribe();
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
        this.ttsService.speak(response);
        this.messages.push({ sender: 'IA', text: response });
      },
      error: () => {
        this.messages.push({ sender: 'IA', text: 'Une erreur s\'est produite' });
      }
    })
    this.input = '';
  }
}
