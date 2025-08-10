import { Injectable, inject, signal, effect } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse, HttpStatusCode } from '@angular/common/http';

import { catchError, Observable, take, tap, throwError } from 'rxjs';

import { UserData } from '../models/common.model';
import { ApiEndpoint, EnvironmentModel } from '../models/environment.model';
import { ENVIRONMENT_TOKEN } from '../../environments/environment.token';
import { JWT_TOKEN, LoginAPIResponse, USER_DATA_STR } from '../models/login.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private router = inject(Router);
  private httpClient = inject(HttpClient);
  private readonly envConfig: EnvironmentModel = inject(ENVIRONMENT_TOKEN);
  private loginError = signal<HttpStatusCode | 0 | undefined>(undefined);

  isAuthenticated = signal<boolean>(true);
  isTokenExpired = signal<boolean>(false);
  userData = signal<UserData | undefined>(undefined);

  constructor() {
    effect(() => {
      if (this.isTokenExpired()) this.logout();
    });

    const token = localStorage.getItem(JWT_TOKEN);
    const userDataStr = localStorage.getItem(USER_DATA_STR);

    if (token && userDataStr) {
      this.isAuthenticated.set(true);
      this.userData.set(JSON.parse(userDataStr));
    }
  }

  login(username: string | null | undefined, password: string | null | undefined): Observable<LoginAPIResponse>  {
    const loginUrl = `${this.envConfig.apiUrl}/${ApiEndpoint.LOGIN}`;
    const loginData = { username, password };

    return this.httpClient.post<LoginAPIResponse>(loginUrl, loginData).pipe(
      take(1),
      tap((apiData: LoginAPIResponse) => {
        const userData: UserData = {
          username: apiData.user.username,
          region: apiData.user.region,
          level: apiData.user.level,
          points: apiData.user.points
        }

        this.isAuthenticated.set(true);
        this.userData.set(userData);
        this.loginError.set(undefined);

        localStorage.setItem(JWT_TOKEN, apiData.jwt_token);
        localStorage.setItem(USER_DATA_STR, JSON.stringify(userData));

        this.router.navigate(['/accueil']);
      }), 
      catchError((httpError: HttpErrorResponse) => {
        this.loginError.set(httpError.status);
        this.isAuthenticated.set(false);
        this.userData.set(undefined);
        return throwError(() => httpError);
      })
    );
  }

  logout() {
    this.isAuthenticated.set(false);
    this.userData.set(undefined);
    this.loginError.set(undefined);

    localStorage.removeItem(JWT_TOKEN);
    localStorage.removeItem(USER_DATA_STR);
    this.router.navigate(['/connexion']);
  }
}
