import { Injectable } from '@angular/core';

declare var webkitSpeechRecognition: any;

@Injectable({
  providedIn: 'root'
})
export class MicService {
  recognition = new webkitSpeechRecognition();
  private isListening: boolean = true;
  public text: string = '';
  public tempWords: string = '';

  constructor() {
    this.recognition.interimResults = true;
    this.recognition.lang ='fr-FR';
  }

  init() {
    this.recognition.addEventListener('result', (event: any) => {
      const transcript = Array.from(event.results)
      .map((result: any) => result[0])
      .map((result: any) => result.transcript)
      .join('');
      this.tempWords = transcript;
    });
  }

  start(){
    this.isListening = true;
    this.recognition.start();
    console.log('Speech recognition started');

    this.recognition.addEventListener('end', () => {
      if (!this.isListening) {
        this.recognition.stop();
        console.log('Speech recognition ended');
      } else {
        this.wordConcat();
        this.recognition.start();
      }
    });
  }

  stop () {
    this.isListening = false;
    this.wordConcat();
    this.recognition.stop();
    console.log('Speech recognition stopped');
    this.text = '';
    this.tempWords = '';
  }

  wordConcat() {
    this.text = `${this.text} ${this.tempWords}`;
    this.tempWords = '';
  }
  
}
