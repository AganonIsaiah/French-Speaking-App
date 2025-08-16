import { Component, inject, OnInit, signal } from '@angular/core';
import { FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { NgxSkeletonLoaderComponent } from 'ngx-skeleton-loader';

import { ApiEndpoint } from '../../models/environment.model';

import { TitleTemplate } from '../../shared/title-template/title-template';
import { ChatReqDTO } from '../../models/chatbot.model';

import { STTService } from '../../services/stt-service';
import { ChatbotService } from '../../services/chatbot-service';

@Component({
  selector: 'conversations',
  imports: [TitleTemplate, MatFormFieldModule, MatInputModule, MatButtonModule, ReactiveFormsModule, MatIconModule, NgxSkeletonLoaderComponent],
  templateUrl: 'conversations.html'
})
export class Conversations implements OnInit {
  isMicOn = signal<boolean>(false);
  userTranscript = signal<string>('');
  isResponseLoading = signal<boolean>(false);

  chatService = inject(ChatbotService);
  private sttService = inject(STTService);

  msgControl = new FormControl('', Validators.required);

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
