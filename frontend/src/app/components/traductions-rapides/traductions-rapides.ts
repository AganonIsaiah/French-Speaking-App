import { Component, inject, OnInit, signal } from '@angular/core';
import { FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { NgxSkeletonLoaderComponent } from 'ngx-skeleton-loader';

import { TitleTemplate } from '../../shared/title-template/title-template';

import { ChatbotService } from '../../services/chatbot-service';
import { STTService } from '../../services/stt-service';

import { ChatReqDTO, TradCorrigeeReqDto } from '../../models/chatbot.model';
import { ApiEndpoint } from '../../models/environment.model';

@Component({
  selector: 'traductions-rapides',
  imports: [TitleTemplate, MatFormFieldModule, MatInputModule, MatButtonModule, ReactiveFormsModule, MatIconModule
    , NgxSkeletonLoaderComponent, MatProgressBarModule
  ],
  templateUrl: './traductions-rapides.html'
})
export class TraductionsRapides implements OnInit {
  isPhrasesLoaded = signal<boolean>(false);
  showLoader = signal<boolean>(false);
  dixPhrases = signal<string[]>([]);
  isMicOn = signal<boolean>(false);
  userTranscript = signal<string>('');
  points = signal<number>(-1);
  index = signal<number>(1);
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

      const french: string = this.dixPhrases()[0];
      const userModel = TradCorrigeeReqDto.buildModel(french, this.userTranscript());

      this.chatService.genTradCorrections(userModel, ApiEndpoint.TRADUCTIONSRAPIDESCORRIGEES).subscribe({
        next: (res) => {
          console.log('Chat response:', res);
          this.points.set(this.points() + res.points)
        },
        error: (err) => console.error('Chat error:', err),
        complete: () => this.isResponseLoading.set(false),
      });

      this.sttService.text = '';
    }

    console.warn(`Transcript : ${this.userTranscript()}`);
  }

  onSubmitMsg() {
    const english = this.msgControl.value;
    if (!english) return;

    this.isResponseLoading.set(true);
    const french: string = this.dixPhrases()[1];
    const userModel = TradCorrigeeReqDto.buildModel(french, english);

    this.chatService.genTradCorrections(userModel, ApiEndpoint.TRADUCTIONSRAPIDESCORRIGEES).subscribe({
      next: (res) => {
        this.msgControl.reset();
        console.log('Response:', res);
        this.points.set(res.points)
      },
      error: (err) => console.error('Error:', err),
      complete: () => this.isResponseLoading.set(false)
    });
  }

  onReq10Phrases() {
    this.isPhrasesLoaded.set(false);
    this.showLoader.set(true);
    const userModel = ChatReqDTO.buildModel();

    this.chatService.gen10Sentences(userModel.level, ApiEndpoint.DIXPHRASES).subscribe({
      next: (res) => {
        console.log("Response:", res);
        this.dixPhrases.set(res);
      },
      error: (err) => console.error('Error:', err),
      complete: () => {
        this.isPhrasesLoaded.set(true);
        this.showLoader.set(false);
        this.points.set(70);
      }
    });
  }
}
