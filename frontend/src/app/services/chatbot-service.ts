import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { EnvironmentModel, ApiEndpoint } from '../models/environment.model';
import { ENVIRONMENT_TOKEN } from '../../environments/environment.token';
import { ChatReqDTO, ChatMessage } from '../models/chatbot.model';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {
  private http = inject(HttpClient);
  private readonly envConfig: EnvironmentModel = inject(ENVIRONMENT_TOKEN);
  private readonly baseUrl = this.envConfig.apiUrl;

  resList = signal<ChatMessage[]>([]);
  suggestList = signal<string[]>([]);

  generateSuggestions(req: ChatReqDTO): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/${ApiEndpoint.ASSIST}`, req, { responseType: 'text' });
  }

  generateChat(req: ChatReqDTO): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/${ApiEndpoint.CHAT}`, req, { responseType: 'text' })
      .pipe(tap(res => this.addResponse(req, res)));
  }

  addResponse(req: ChatReqDTO, res: string) {
    this.resList.update(prev => [
      ...prev,
      { sender: 'Vous', message: req.message },
      { sender: 'Ai', message: res}
    ]);
  }
}
