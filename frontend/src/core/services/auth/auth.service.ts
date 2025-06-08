import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SignupData } from '../../models/signup-data';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private url = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  signup(data: SignupData): Observable<any> {
    return this.http.post(`${this.url}/signup`, data);
  }

  login(credentials: { username: string, password: string }): Observable<any> {
    return this.http.post(`${this.url}/login`, credentials);
  }

  saveUser(user: any) {
    // console.log("Save user:", user);
    localStorage.setItem('user', JSON.stringify(user));
  }

  getUser() {
    if (typeof localStorage !== 'undefined')
      return JSON.parse(localStorage.getItem('user') || '{}')
    return {}
  }

  logout() {
    localStorage.removeItem('user')
  }
}
