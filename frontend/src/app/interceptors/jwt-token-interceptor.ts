import { HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { JWT_TOKEN } from '../models/login.model';

export const jwtTokenInterceptor: HttpInterceptorFn = 
  (request: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {
    const token = localStorage.getItem(JWT_TOKEN) ?? '';
    request = request.clone({
      setHeaders: {
        Authorization: token ? `Bearer ${token}` : '',
      }
    });

   return next(request);
  };
