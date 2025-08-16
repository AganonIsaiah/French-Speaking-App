import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Observable, tap, catchError, throwError } from 'rxjs';

import { EnvironmentModel, ApiEndpoint } from '../models/environment.model';
import { ENVIRONMENT_TOKEN } from '../../environments/environment.token';
import { ChatReqDTO, ChatMessage, TradCorrigeeReqDto } from '../models/chatbot.model';
import { UserLevel } from '../models/common.model';

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
  suggestList = signal<string[]>([]);

  gen10Sentences(level: string, endpoint: ApiEndpoint ): Observable<string[]> {
    return this.http.post<string[]>(`${this.baseUrl}/${endpoint}`, { level })
    .pipe (
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

  genTradCorrections(req: TradCorrigeeReqDto, endpoint: ApiEndpoint): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/${endpoint}`, req, { responseType: 'text' })
      .pipe(
        tap(res => {
          this.addResponse(req.translatedEnglish, res);
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

  addResponse(req: string, res: string) {
    this.resList.update(prev => [
      ...prev,
      { sender: 'Vous', message: req },
      { sender: 'Ai', message: res }
    ]);
  }
}
