import { HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from "@angular/common/http"
import { Observable } from "rxjs"

import { JWT_TOKEN } from "../models/login.model"

export const jwtTokenInterceptor: HttpInterceptorFn = 
(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> => {
  const token = localStorage.getItem(JWT_TOKEN) ?? '';

  req = req.clone({
    setHeaders: {
      Authorization: token ? `Bearer ${token}` : '',
    }
  });

  return next(req);
}
