import { Injectable, inject, signal, effect } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, take, tap, throwError } from 'rxjs';

import { UserData } from '../models/common.model';
import { ApiEndpoint, EnvironmentModel } from '../models/environment.model';
import { ENVIRONMENT_TOKEN } from '../../environments/environment.token';
import { LoginAPIResponse, SignupRequest, SignupResponse, UpdateLevelResponse } from '../models/login.model';
import { CookieUtils } from '../utils/cookie.utils';

type prop = null | undefined;

@Injectable({ providedIn: 'root' })
export class AuthService {
  private router = inject(Router);
  private httpClient = inject(HttpClient);
  private readonly envConfig: EnvironmentModel = inject(ENVIRONMENT_TOKEN);

  isAuthenticated = signal<boolean>(false);
  isTokenExpired = signal<boolean>(false);
  userData = signal<UserData | undefined>(undefined);

  constructor() {
    effect(() => {
      if (this.isTokenExpired()) this.logout();
    });

    const token = CookieUtils.get('jwt_token');
    const userStr = CookieUtils.get('user');

    if (token && userStr) {
      this.isAuthenticated.set(true);
      this.userData.set(JSON.parse(userStr));
    } else {
      this.isAuthenticated.set(false);
    }
  }

  signup(
    username: string | prop,
    email: string | prop,
    region: string | prop,
    level: string | prop,
    password: string | prop
  ): Observable<SignupResponse> {
    const signupData: SignupRequest = {
      username: username || '',
      email: email || '',
      password: password || '',
      region: region || '',
      level: level || '',
    };

    return this.httpClient
      .post<SignupResponse>(`${this.envConfig.apiUrl}/${ApiEndpoint.SIGNUP}`, signupData)
      .pipe(
        take(1),
        tap(() => this.router.navigate(['/connexion'])),
        catchError((httpError: HttpErrorResponse) => {
          this.isAuthenticated.set(false);
          this.userData.set(undefined);
          return throwError(() => httpError);
        })
      );
  }

  login(username: string | prop, password: string | prop): Observable<LoginAPIResponse> {
    return this.httpClient
      .post<LoginAPIResponse>(`${this.envConfig.apiUrl}/${ApiEndpoint.LOGIN}`, { username, password })
      .pipe(
        take(1),
        tap((apiData: LoginAPIResponse) => {
          const userData: UserData = {
            username: apiData.user.username,
            email: apiData.user.email,
            region: apiData.user.region,
            level: apiData.user.level,
          };

          this.isAuthenticated.set(true);
          this.userData.set(userData);

          CookieUtils.set('jwt_token', apiData.jwt_token);
          CookieUtils.set('user', JSON.stringify(userData));

          this.router.navigate(['/accueil']);
        }),
        catchError((httpError: HttpErrorResponse) => {
          this.isAuthenticated.set(false);
          this.userData.set(undefined);
          return throwError(() => httpError);
        })
      );
  }

  logout() {
    this.isAuthenticated.set(false);
    this.userData.set(undefined);

    CookieUtils.remove('jwt_token');
    CookieUtils.remove('user');

    this.router.navigate(['/connexion']);
  }

  updateLevel(newLevel: string): Observable<UpdateLevelResponse> {
    const token = CookieUtils.get('jwt_token');

    if (!token) {
      return throwError(() => new Error('User not authenticated'));
    }

    return this.httpClient
      .put<UpdateLevelResponse>(
        `${this.envConfig.apiUrl}/${ApiEndpoint.UPDATELEVEL}`,
        { level: newLevel },
        { headers: { Authorization: `Bearer ${token}` } }
      )
      .pipe(
        take(1),
        tap((response) => {
          const currentUser = this.userData();
          if (currentUser) {
            const updatedUser: UserData = { ...currentUser, level: response.level };
            this.userData.set(updatedUser);
            CookieUtils.set('user', JSON.stringify(updatedUser));
          }
        }),
        catchError((httpError: HttpErrorResponse) => throwError(() => httpError))
      );
  }
}
