import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';


@Injectable({
  providedIn: 'root'
})
export class TtsService {
  private frenchVoice: SpeechSynthesisVoice | null = null;
  private isBrowser: boolean;

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {
    this.isBrowser = isPlatformBrowser(this.platformId);

    if (this.isBrowser) {
      this.loadVoices();
      window.speechSynthesis.onvoiceschanged = () => this.loadVoices();
    }
  }

  private loadVoices() {
    if (!this.isBrowser) return;

    const voices = window.speechSynthesis.getVoices();
   
    this.frenchVoice = voices.find(voice =>
      voice.lang === 'fr-FR' && (
        voice.name.includes('Google')  // Chrome on desktop
      )
    ) || voices.find(voice => voice.lang.startsWith('fr')) || null;

    if (!this.frenchVoice) {
      console.warn('⚠️ French voice not found. Default system voice will be used.');
    } else {
      console.log("✅ Selected voice:", this.frenchVoice.name);
    }
  }

  speak(text: string) {
    if (!this.isBrowser || !text.trim()) return;

    const utterance = new SpeechSynthesisUtterance(text);
    utterance.lang = 'fr-FR';
    if (this.frenchVoice) {
      utterance.voice = this.frenchVoice;
    }

    window.speechSynthesis.cancel();
    window.speechSynthesis.speak(utterance);
  }
}
