import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { EnvironmentModel, ApiEndpoint } from '../models/environment.model';
import { ENVIRONMENT_TOKEN } from '../../environments/environment.token';
import { ChatReqDTO } from '../models/chatbot.model';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {
  private http = inject(HttpClient);
  private readonly envConfig: EnvironmentModel = inject(ENVIRONMENT_TOKEN);
  private readonly baseUrl = `${this.envConfig.apiUrl}/${ApiEndpoint.CHAT}`;

  resList = signal<string[]>([]);

  generateChat(req: ChatReqDTO): Observable<string> {
    return this.http.post(
      `${this.baseUrl}`, req, { responseType: 'text' })
      .pipe(tap(res => this.addResponse(res)));
  }

  addResponse(res: string) {
    this.resList.update(prev => [...prev, res]);
  }
}
