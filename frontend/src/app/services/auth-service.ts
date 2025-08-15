import { Injectable, inject, signal, effect } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse, HttpStatusCode } from '@angular/common/http';

import { catchError, Observable, take, tap, throwError } from 'rxjs';

import { UserData } from '../models/common.model';
import { ApiEndpoint, EnvironmentModel } from '../models/environment.model';
import { ENVIRONMENT_TOKEN } from '../../environments/environment.token';
import { JWT_TOKEN, LoginAPIResponse, USER_DATA_STR, SignupRequest, SignupResponse } from '../models/login.model';


type prop = null | undefined

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private router = inject(Router);
  private httpClient = inject(HttpClient);
  private readonly envConfig: EnvironmentModel = inject(ENVIRONMENT_TOKEN);
  private loginError = signal<HttpStatusCode | 0 | undefined>(undefined);

  isAuthenticated = signal<boolean>(false);
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
    } else {
      this.logout();
    }
  }

 signup(username: string | null | undefined, email: string | null | undefined, region: string | null | undefined, level: string | null | undefined, password: string | null | undefined): Observable<SignupResponse> {
  const signupUrl = `${this.envConfig.apiUrl}/${ApiEndpoint.SIGNUP}`;
  const signupData: SignupRequest = {
    username: username || '',
    email: email || '',
    password: password || '',
    region: region || '',
    level: level || '',
    points: 0
  }

  return this.httpClient.post<SignupResponse>(signupUrl, signupData).pipe(
    take(1),
    tap(response => {
      console.log('Signup success message:', response.message);
      this.router.navigate(['/connexion']);
    }),
    catchError((httpError: HttpErrorResponse) => {
      this.isAuthenticated.set(false);
      this.userData.set(undefined);
      return throwError(() => httpError);
    })
  );
}


  login(username: string | prop, password: string | prop): Observable<LoginAPIResponse> {
    const loginUrl = `${this.envConfig.apiUrl}/${ApiEndpoint.LOGIN}`;
    const loginData = { username, password };

    return this.httpClient.post<LoginAPIResponse>(loginUrl, loginData).pipe(
      take(1),
      tap((apiData: LoginAPIResponse) => {
        const userData: UserData = {
          username: apiData.user.username,
          email: apiData.user.email,
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
