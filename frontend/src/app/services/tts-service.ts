import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TTSService {
  private synth = window.speechSynthesis;
  private frenchVoice?: SpeechSynthesisVoice;

  constructor() {
    this.loadVoices();
    this.synth.onvoiceschanged = () => this.loadVoices();
  }

  private loadVoices() {
    const voices = this.synth.getVoices();
    this.frenchVoice = voices.find(voice =>
      voice.lang.startsWith('fr') && voice.name.toLowerCase().includes('google')
    ) || voices.find(voice => voice.lang.startsWith('fr'));
  }

  speak(text: string) {
    if (!this.frenchVoice) {
      this.loadVoices();
    }

    const utterance = new SpeechSynthesisUtterance(text);
    utterance.voice = this.frenchVoice || null;
    utterance.lang = 'fr-FR';

    this.synth.speak(utterance);
  }

  cancel() {
    this.synth.cancel();
  }
}
