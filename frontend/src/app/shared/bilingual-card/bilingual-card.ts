import { Component, input, signal } from '@angular/core';

import { LanguageCard } from '../../models/common.model';

import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'bilingual-card',
  imports: [MatIconModule],
  template: `
  <div class="border border-[#e1e3e5] !mt-4 !m-auto max-w-350 flex justify-center items-center gap-2 !bg-[#f3f4f6] shadow-lg !p-2 !py-4 rounded-lg">
   <p class="whitespace-pre-line text-center leading-6 !text-[16px]">{{ card()[currentLang()]}}</p>
      <button 
        class="!ml-8 green-btn"
        (click)="toggleLang()"
      >
        {{ currentLang() === 'en' ? 'FR' : 'EN'}}
      </button>
  </div>
  `
})
export class BilingualCard {
  currentLang = signal<'en' | 'fr'>('fr');
  card = input.required<LanguageCard>();

  toggleLang() {
    this.currentLang.set(this.currentLang() === 'en' ? 'fr' : 'en');
  }
}