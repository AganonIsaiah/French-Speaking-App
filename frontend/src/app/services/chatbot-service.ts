import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { EnvironmentModel, ApiEndpoint } from '../../environments/environment.model';
import { ENVIRONMENT_TOKEN } from '../../environments/environment.token';
import { ChatReqDTO } from './chatbot.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {
  private http = inject(HttpClient);
  private readonly envConfig: EnvironmentModel = inject(ENVIRONMENT_TOKEN);
  private readonly baseUrl = `${this.envConfig.apiUrl}/${ApiEndpoint.CHAT}`;


  generateChat(req: ChatReqDTO): Observable<string> {
    console.log(this.baseUrl)
    return this.http.post(
      `${this.baseUrl}`, req, { responseType: 'text' }
    )
  }
}
