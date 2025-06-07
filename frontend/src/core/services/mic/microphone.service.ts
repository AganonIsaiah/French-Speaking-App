import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ChatService } from '../chat/chat.service';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class MicrophoneService {
  private recognition: any;
  private silenceTimeout: any;
  private silenceDelay = 2000;
  private isVoiceDetected = false;
  private isListening = false;

  currentTranscript$ = new BehaviorSubject<string>('');
  isSpeaking$ = new BehaviorSubject<boolean>(false);
  chatResponse$ = new BehaviorSubject<string>('');


  constructor(
    private zone: NgZone,
    private chatService: ChatService,
    private authService: AuthService
  ) {
    if (typeof window === 'undefined') { return; }

    const SpeechRecognition = (window as any).webkitSpeechRecognition || (window as any).SpeechRecognition;

    if (!SpeechRecognition) {
      alert('Speech Recognition not supported in this browser.');
      return;
    }

    this.recognition = new SpeechRecognition();
    this.recognition.lang = 'fr-FR';
    this.recognition.continuous = true;
    this.recognition.interimResults = true;

    this.recognition.onresult = (event: any) => {
      let finalTranscript = '';
      let interimTranscript = '';

      for (let i = event.resultIndex; i < event.results.length; ++i) {
        const transcriptPart = event.results[i][0].transcript;
        if (event.results[i].isFinal) {
          finalTranscript += transcriptPart + ' ';
        } else {
          interimTranscript += transcriptPart;
        }
      }

      this.zone.run(() => {
        if ((finalTranscript || interimTranscript) && !this.isVoiceDetected) {
          this.onVoiceDetected();
        }

        if (finalTranscript !== '') {
          const updatedCurrentTranscript: string = this.currentTranscript$.value + finalTranscript;
          this.currentTranscript$.next(updatedCurrentTranscript);
          if (/[a-zA-ZÀ-ÿ]/.test(updatedCurrentTranscript)) {
            // console.log('📝 Current transcript:', updatedCurrentTranscript);



            this.chatService.conversation(
              updatedCurrentTranscript.trim(),
              this.authService.getUser().proficiency,
              this.authService.getUser().username
            ).subscribe({
              next: (response) => {
                this.chatResponse$.next(response); 
                // console.log('Chat API response:', response)
              },
              error: (err) => {
                // console.error('Chat API error:', err);
                this.chatResponse$.next('Une erreur s\'est produite');
              }
            });
          }
        }

        this.resetSilenceTimeout();
      });
    };

    this.recognition.onend = () => {
      this.isListening = false;
      this.zone.run(() => {
        this.isSpeaking$.next(false);
      });
      // console.log('⏹️ Recognition ended');
      // No auto-restart!
    };

    this.recognition.onerror = (event: any) => {
      // console.error('🚨 Speech recognition error:', event.error);
      // No auto-restart!
    };
  }

  private resetSilenceTimeout() {
    clearTimeout(this.silenceTimeout);
    this.silenceTimeout = setTimeout(() => {
      if (this.isVoiceDetected) {
        this.onSilenceDetected();
      }
    }, this.silenceDelay);
  }

  private startListening() {
    if (!this.isListening) {
      try {
        this.recognition.start();
        this.isListening = true;
        // console.log('🎙️ Listening started...');
      } catch (e) {
        // console.warn('⚠️ Recognition already started?', e);
      }
    }
  }

  private stopListening() {
    if (this.isListening) {
      this.recognition.stop();
      this.isListening = false;
      this.isVoiceDetected = false;
      this.zone.run(() => {
        this.isSpeaking$.next(false);
      });
      clearTimeout(this.silenceTimeout);
      // console.log('🛑 Listening stopped.');
    }
  }

  private onVoiceDetected() {
    if (!this.isVoiceDetected) {
      this.isVoiceDetected = true;
      this.zone.run(() => {
        this.isSpeaking$.next(true);
      });
      // console.log('🗣️ Voice detected, starting new transcript');
      this.currentTranscript$.next('');
    }
  }

  private onSilenceDetected() {
    if (this.isVoiceDetected) {
      // console.log('😶 Silence detected, transcript ready');
      this.isVoiceDetected = false;
      this.zone.run(() => {
        this.isSpeaking$.next(false);
      });
    }
    clearTimeout(this.silenceTimeout);
  }

  // Only toggle listening on button press
  toggle() {
    if (this.isListening) {
      this.stopListening();
    } else {
      this.startListening();
    }
  }
}