import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZonelessChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';

import { jwtTokenInterceptor } from './interceptors/jwt-token.interceptor';
import { ENVIRONMENT_TOKEN } from '../environments/environment.token';
import { environment } from '../environments/environment';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZonelessChangeDetection(),
    provideRouter(routes),
    provideHttpClient(withInterceptors([jwtTokenInterceptor])),
    { provide: ENVIRONMENT_TOKEN, useValue: environment }
  ]
};
