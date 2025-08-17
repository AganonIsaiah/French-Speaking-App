import { Component, inject, OnInit, signal } from '@angular/core';
import { FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { NgxSkeletonLoaderComponent } from 'ngx-skeleton-loader';

import { TitleTemplate } from '../../shared/title-template/title-template';
import { BilingualCard } from '../../shared/bilingual-card/bilingual-card';

import { ChatbotService } from '../../services/chatbot-service';
import { STTService } from '../../services/stt-service';

import { ChatReqDTO, TradCorrigeeReqDto } from '../../models/chatbot.model';
import { ApiEndpoint } from '../../models/environment.model';
import { LanguageCard } from '../../models/common.model';

@Component({
  selector: 'traductions-rapides',
  imports: [TitleTemplate, MatFormFieldModule, MatInputModule, MatButtonModule, ReactiveFormsModule, MatIconModule
    , NgxSkeletonLoaderComponent, MatProgressBarModule, BilingualCard
  ],
  templateUrl: './traductions-rapides.html'
})
export class TraductionsRapides implements OnInit {
  isPhrasesLoaded = signal<boolean>(false);
  showLoader = signal<boolean>(false);
  showStartMsg = signal<boolean>(true);
  dixPhrases = signal<string[]>([]);
  isMicOn = signal<boolean>(false);
  userTranscript = signal<string>('');
  points = signal<number>(-1);
  index = signal<number>(1);
  isResponseLoading = signal<boolean>(false);

  chatService = inject(ChatbotService);
  private sttService = inject(STTService);

  msgControl = new FormControl('', Validators.required);

  startMessage: LanguageCard = {
    en: `Welcome to the Fast Translations game mode!
    • In this game mode, 10 random French sentences will be generated based on your proficiency level.
    • You must translate each sentence (with the microphone or text input component).
    • After each translation, you will receive a score and feedback based on your accuracy.
    `,
    fr: `Bienvenue dans le mode de jeu Traductions rapides!
    • Dans ce mode de jeu, 10 phrases françaises aléatoires seront générées en fonction de votre niveau de compétence.
    • Vous devez traduire chaque phrase (à l'aide du microphone ou du composant de saisie de texte).
    • Après chaque traduction, vous recevrez une note et des commentaires basés sur votre précision.`
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

      const french: string = this.dixPhrases()[this.index()];
      const userModel = TradCorrigeeReqDto.buildModel(french, this.userTranscript());

      this.chatService.genTradCorrections(userModel, ApiEndpoint.TRADUCTIONSRAPIDESCORRIGEES).subscribe({
        next: (res) => {
          console.log('Chat response:', res);
          this.points.set(this.points() + (res.points / 10));

          this.chatService.resList.update(prev => [
            ...prev,
            { sender: 'gen', message: this.dixPhrases()[this.index() + 1] }]);
        },
        error: (err) => console.error('Chat error:', err),
        complete: () => {
          this.isResponseLoading.set(false);
          this.index.set(this.index() + 1);
        }
      });

      this.sttService.text = '';
    }

    console.warn(`Transcript : ${this.userTranscript()}`);
  }

  onSubmitMsg() {
    const english = this.msgControl.value;
    if (!english) return;

    this.isResponseLoading.set(true);

    const french: string = this.dixPhrases()[this.index()];
    const userModel = TradCorrigeeReqDto.buildModel(french, english);

    this.chatService.genTradCorrections(userModel, ApiEndpoint.TRADUCTIONSRAPIDESCORRIGEES).subscribe({
      next: (res) => {
        this.msgControl.reset();
        console.log('Response:', res);
        this.points.set(this.points() + (res.points / 10));

        this.chatService.resList.update(prev => [
          ...prev,
          { sender: 'gen', message: this.dixPhrases()[this.index() + 1] }]);

      },
      error: (err) => console.error('Error:', err),
      complete: () => {
        this.isResponseLoading.set(false);
        this.index.set(this.index() + 1);
      }
    });
  }

  onReq10Phrases() {
    this.showStartMsg.set(false);
    this.isPhrasesLoaded.set(false);
    this.showLoader.set(true);
    const userModel = ChatReqDTO.buildModel();

    this.chatService.gen10Sentences(userModel.level, ApiEndpoint.DIXPHRASES).subscribe({
      next: (res) => {
        console.log("Response:", res);
        this.dixPhrases.set(res);
        this.chatService.resList.set([{ sender: 'gen', message: res[1] }]);
      },
      error: (err) => console.error('Error:', err),
      complete: () => {
        this.isPhrasesLoaded.set(true);
        this.showLoader.set(false);
        this.points.set(0);
        this.index.set(1);
      }
    });
  }
}
