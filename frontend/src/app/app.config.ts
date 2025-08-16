import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZonelessChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideNgxSkeletonLoader } from 'ngx-skeleton-loader';

import { routes } from './app.routes';

import { jwtTokenInterceptor } from './interceptors/jwt-token-interceptor';
import { httpInterceptor } from './interceptors/http-interceptor';
import { ENVIRONMENT_TOKEN } from '../environments/environment.token';
import { environment } from '../environments/environment';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZonelessChangeDetection(),
    provideRouter(routes),
    provideHttpClient(withInterceptors([jwtTokenInterceptor, httpInterceptor])),
    { provide: ENVIRONMENT_TOKEN, useValue: environment },
    provideNgxSkeletonLoader(),
  ]
};
