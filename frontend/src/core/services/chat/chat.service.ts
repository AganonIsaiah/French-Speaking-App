import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private url = 'http://localhost:8080/api/chat'

  constructor(private http: HttpClient) { }

  conversation(message: string, level: string, username: string): Observable<any> {
    return this.http.post(`${this.url}/conversation`, { message, level, username }, { responseType: 'text' });
  }

  learning(message: string, level: string, username: string): Observable<any> {
    return this.http.post(`${this.url}/learning`, { message, level, username }, { responseType: 'text' });
  }
}
