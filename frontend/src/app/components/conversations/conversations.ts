import { Component, inject, OnInit, signal } from '@angular/core';
import { FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { NgxSkeletonLoaderComponent } from 'ngx-skeleton-loader';

import { ApiEndpoint } from '../../models/environment.model';
import { LanguageCard } from '../../models/common.model';
import { ChatReqDTO } from '../../models/chatbot.model';

import { TitleTemplate } from '../../shared/title-template/title-template';
import { BilingualCard } from '../../shared/bilingual-card/bilingual-card';
import { STTService } from '../../services/stt-service';
import { ChatbotService } from '../../services/chatbot-service';

@Component({
  selector: 'conversations',
  imports: [TitleTemplate, MatFormFieldModule, MatInputModule, MatButtonModule, ReactiveFormsModule, MatIconModule, NgxSkeletonLoaderComponent, BilingualCard],
  templateUrl: 'conversations.html',
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
• Practice your French speaking with AI.
• Use the text input or microphone to begin.`,
    fr: `Bienvenue dans le mode Conversation sans fin !
• Pratiquez votre français avec l'IA.
• Utilisez la saisie de texte ou le microphone pour commencer.`,
  };

  ngOnInit(): void {
    this.sttService.init();
    this.chatService.resList.set([]);
  }

  onToggleMic() {
    this.isMicOn.set(!this.isMicOn());

    if (this.isMicOn()) {
      this.sttService.start();
    } else {
      this.sttService.stop();
      this.userTranscript.set(this.sttService.text);

      if (!this.userTranscript()) return;

      this.showStartupMsg.set(false);
      this.isResponseLoading.set(true);
      const req = ChatReqDTO.buildModel(this.userTranscript());

      this.chatService.genChat(req, ApiEndpoint.CONVERSATIONS).subscribe({
        error: (err) => console.error('Chat error:', err),
        complete: () => this.isResponseLoading.set(false),
      });

      this.sttService.text = '';
    }
  }

  onSubmitMsg() {
    const msg = this.msgControl.value;
    if (!msg) return;

    this.showStartupMsg.set(false);
    this.isResponseLoading.set(true);
    const req = ChatReqDTO.buildModel(msg);

    this.chatService.genChat(req, ApiEndpoint.CONVERSATIONS).subscribe({
      next: () => this.msgControl.reset(),
      error: (err) => console.error('Error:', err),
      complete: () => this.isResponseLoading.set(false),
    });
  }
}
