import { Component, inject, OnInit, signal } from '@angular/core';
import { FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { NgxSkeletonLoaderComponent } from 'ngx-skeleton-loader';

import { ApiEndpoint } from '../../models/environment.model';
import { LanguageCard } from '../../models/common.model';

import { TitleTemplate } from '../../shared/title-template/title-template';
import { BilingualCard } from '../../shared/bilingual-card/bilingual-card';
import { ChatReqDTO } from '../../models/chatbot.model';

import { STTService } from '../../services/stt-service';
import { ChatbotService } from '../../services/chatbot-service';

@Component({
  selector: 'conversations',
  imports: [TitleTemplate, MatFormFieldModule, MatInputModule, MatButtonModule, ReactiveFormsModule, MatIconModule, NgxSkeletonLoaderComponent, BilingualCard],
  templateUrl: 'conversations.html'
})
export class Conversations implements OnInit {
  isMicOn = signal<boolean>(false);
  userTranscript = signal<string>('');
  isResponseLoading = signal<boolean>(false);
  showStartupMsg = signal<boolean>(true);

  chatService = inject(ChatbotService);
  private sttService = inject(STTService);

  msgControl = new FormControl('', Validators.required);

  startupText: LanguageCard = {
    en: `Welcome to the Endless Conversation mode!
    • In this game mode, you can practice your French speaking with AI!
    • You can use the text input or microphone component to begin your conversation.
    `,
    fr: `Bienvenue dans le mode Conversation sans fin !
• Dans ce mode de jeu, vous pouvez pratiquer votre français avec l'IA !
• Vous pouvez utiliser la saisie de texte ou le microphone pour commencer votre conversation.`
  }

  ngOnInit(): void {
    this.sttService.init();
  }

  onToggleMic() {
    this.isMicOn.set(!this.isMicOn());
    this.isResponseLoading.set(true);

    if (this.isMicOn()) {
      this.sttService.start();
    } else {
      this.sttService.stop();
      this.userTranscript.set(this.sttService.text);

      const userModel = ChatReqDTO.buildModel(this.userTranscript());

      this.chatService.genChat(userModel, ApiEndpoint.CONVERSATIONS).subscribe({
        next: (response) => console.log('Chat response:', response),
        error: (err) => console.error('Chat error:', err),
        complete: () => this.isResponseLoading.set(false),
      });

      this.sttService.text = '';
    }

    console.warn(`Transcript : ${this.userTranscript()}`);
  }

  onSubmitMsg() {
    const msg = this.msgControl.value;
    if (!msg) return;

    this.showStartupMsg.set(false);
    this.isResponseLoading.set(true);
    const userModel = ChatReqDTO.buildModel(msg);

    this.chatService.genChat(userModel, ApiEndpoint.CONVERSATIONS).subscribe({
      next: (res: string) => {
        this.msgControl.reset();
        console.log('Response:', res);
      },
      error: (err) => console.error('Error:', err),
      complete: () => this.isResponseLoading.set(false)
    });
  }
}
