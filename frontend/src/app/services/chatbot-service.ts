import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { EnvironmentModel, ApiEndpoint } from '../models/environment.model';
import { ENVIRONMENT_TOKEN } from '../../environments/environment.token';
import { ChatReqDTO, ChatMessage } from '../models/chatbot.model';
import { Observable, tap, catchError, throwError } from 'rxjs';
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

  generateChat(req: ChatReqDTO): Observable<string> {
  return this.http.post(
    `${this.baseUrl}/${ApiEndpoint.CONVERSATIONS}`, req, { responseType: 'text' })
    .pipe(
      tap(res => {
        res = res.replace(/\*/g, '');
        this.addResponse(req, res);
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

  addResponse(req: ChatReqDTO, res: string) {
    this.resList.update(prev => [
      ...prev,
      { sender: 'Vous', message: req.message },
      { sender: 'Ai', message: res}
    ]);
  }
}
