import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Observable, tap, catchError, throwError } from 'rxjs';

import { EnvironmentModel, ApiEndpoint } from '../models/environment.model';
import { ENVIRONMENT_TOKEN } from '../../environments/environment.token';
import { ChatReqDTO, ChatMessage, TradCorrigeeReqDto, TradRapidesResult } from '../models/chatbot.model';

import { TTSService } from './tts-service';

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {
  private http = inject(HttpClient);
  private ttsService = inject(TTSService);
  private readonly envConfig: EnvironmentModel = inject(ENVIRONMENT_TOKEN);
  private readonly baseUrl = this.envConfig.apiUrl;

  resList = signal<ChatMessage[]>([]);

  gen10Sentences(level: string, endpoint: ApiEndpoint): Observable<string[]> {
    return this.http.post<string[]>(`${this.baseUrl}/${endpoint}`, { level })
      .pipe(
        tap(sentences => {
          console.log('Received sentences:', sentences);

        }),
        catchError(error => {
          if (error.status === 401) {
            console.warn('Unauthorized - please login again');
          }
          return throwError(() => error);
        })
      );
  }

  genTradCorrections(req: TradCorrigeeReqDto, endpoint: ApiEndpoint): Observable<TradRapidesResult> {
    return this.http.post<TradRapidesResult>(`${this.baseUrl}/${endpoint}`, req)
      .pipe(
        tap(res => {
          this.addResponse(req.translatedEnglish, res.feedback, res.points);
          this.ttsService.speak(`${res.feedback} + Le score est de ${res.points}`);
          console.log(`Score: ${res.points}`, `Feedback: ${res.feedback}`);
        }),
        catchError(error => {
          if (error.status === 401) {
            console.warn('Unauthorized - please login again');
          }
          return throwError(() => error);
        })
      );
  }

  genChat(req: ChatReqDTO, endpoint: ApiEndpoint): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/${endpoint}`, req, { responseType: 'text' })
      .pipe(
        tap(res => {
          res = res.replace(/\*/g, '');
          this.addResponse(req.message, res);
          this.ttsService.speak(res);
        }),
        catchError(error => {
          if (error.status === 401) {
            console.warn('Unauthorized - please login again');
          }
          return throwError(() => error);
        })
      );
  }

  addResponse(req: string, res: string, points?: number) {
    this.resList.update(prev => [
      ...prev,
      { sender: 'Vous', message: req },
      { sender: 'Ai', message: res, points: points }
    ]);
  }
}