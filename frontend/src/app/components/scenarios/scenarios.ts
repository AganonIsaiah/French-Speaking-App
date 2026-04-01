import { Component, inject, OnInit, signal } from '@angular/core';
import { FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { NgxSkeletonLoaderComponent } from 'ngx-skeleton-loader';

import { ApiEndpoint } from '../../models/environment.model';
import { ScenarioReqDTO } from '../../models/chatbot.model';
import { TitleTemplate } from '../../shared/title-template/title-template';
import { STTService } from '../../services/stt-service';
import { ChatbotService } from '../../services/chatbot-service';

export interface Scenario {
  key: string;
  title: string;
  description: string;
  icon: string;
  character: string;
}

@Component({
  selector: 'scenarios',
  imports: [TitleTemplate, MatFormFieldModule, MatInputModule, MatButtonModule, ReactiveFormsModule, MatIconModule, NgxSkeletonLoaderComponent],
  templateUrl: './scenarios.html',
})
export class Scenarios implements OnInit {
  selectedScenario = signal<Scenario | null>(null);
  isMicOn = signal<boolean>(false);
  userTranscript = signal<string>('');
  isResponseLoading = signal<boolean>(false);

  chatService = inject(ChatbotService);
  private sttService = inject(STTService);

  msgControl = new FormControl('', Validators.required);

  scenarios: Scenario[] = [
    {
      key: 'cafe',
      title: 'Au Café',
      description: 'Commandez un café, un croissant, ou discutez avec le serveur dans un café parisien.',
      icon: 'coffee',
      character: 'un serveur sympathique dans un café parisien',
    },
    {
      key: 'aeroport',
      title: 'À l\'Aéroport',
      description: 'Enregistrez-vous, demandez votre porte d\'embarquement, ou gérez un problème de bagage.',
      icon: 'flight',
      character: 'un agent d\'accueil à l\'aéroport Charles de Gaulle',
    },
    {
      key: 'marche',
      title: 'Au Marché',
      description: 'Achetez des fruits et légumes, négociez les prix, et pratiquez le vocabulaire du quotidien.',
      icon: 'storefront',
      character: 'un vendeur chaleureux au marché en plein air',
    },
    {
      key: 'medecin',
      title: 'Chez le Médecin',
      description: 'Décrivez vos symptômes, comprenez les prescriptions, et pratiquez le vocabulaire médical.',
      icon: 'medical_services',
      character: 'un médecin généraliste bienveillant',
    },
  ];

  ngOnInit(): void {
    this.sttService.init();
    this.chatService.resList.set([]);
  }

  onSelectScenario(scenario: Scenario) {
    this.selectedScenario.set(scenario);
    this.chatService.resList.set([]);
    this.isResponseLoading.set(true);

    const req = ScenarioReqDTO.buildModel('Bonjour', scenario.key, scenario.character, true);

    this.chatService.genScenario(req, ApiEndpoint.SCENARIOS, true).subscribe({
      error: (err) => console.error('Scenario intro error:', err),
      complete: () => this.isResponseLoading.set(false),
    });
  }

  onBackToSelection() {
    this.selectedScenario.set(null);
    this.chatService.resList.set([]);
  }

  onToggleMic() {
    this.isMicOn.set(!this.isMicOn());
    const scenario = this.selectedScenario();
    if (!scenario) return;

    if (this.isMicOn()) {
      this.sttService.start();
    } else {
      this.sttService.stop();
      this.userTranscript.set(this.sttService.text);

      if (!this.userTranscript()) return;

      this.isResponseLoading.set(true);
      const req = ScenarioReqDTO.buildModel(this.userTranscript(), scenario.key, scenario.character);

      this.chatService.genScenario(req, ApiEndpoint.SCENARIOS).subscribe({
        error: (err) => console.error('Scenario error:', err),
        complete: () => this.isResponseLoading.set(false),
      });

      this.sttService.text = '';
    }
  }

  onSubmitMsg() {
    const msg = this.msgControl.value;
    if (!msg) return;
    const scenario = this.selectedScenario();
    if (!scenario) return;

    this.isResponseLoading.set(true);
    const req = ScenarioReqDTO.buildModel(msg, scenario.key, scenario.character);

    this.chatService.genScenario(req, ApiEndpoint.SCENARIOS).subscribe({
      next: () => this.msgControl.reset(),
      error: (err) => console.error('Error:', err),
      complete: () => this.isResponseLoading.set(false),
    });
  }
}
