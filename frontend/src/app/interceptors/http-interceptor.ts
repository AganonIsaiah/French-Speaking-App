import { HttpErrorResponse, HttpHandlerFn, HttpInterceptorFn, HttpRequest, HttpStatusCode } from '@angular/common/http';
import { catchError, take } from 'rxjs';
import { inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { AuthService } from '../services/auth-service';
import { Dialog, DialogBoxData } from '../shared/dialog/dialog';

export const httpInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn) => {

  const authService = inject(AuthService);
  const dialog = inject(MatDialog);

  return next(req).pipe(
    catchError((err: HttpErrorResponse) => {
      const isJwtInvalid: boolean = !req.credentials;
      const isLoggedIn: boolean = /\/api\/chat\b/.test(req.url);

      console.log(isLoggedIn);
      if (!isLoggedIn && (err.status === HttpStatusCode.Unauthorized || err.status === HttpStatusCode.Forbidden || err.status === HttpStatusCode.BadRequest))
        return next(req);

      const errorTitle = err.status === 0 ? 'Internal Server Error' : `Error ${err.status}`;
      const errorMsg = isJwtInvalid ? 'Token is Invalid. You will be redirected to the Login page.' : 'An unexpected error occurred. Please try again later.';

      dialog.open(Dialog, <DialogBoxData> {
        data: {
          title: errorTitle,
          message: errorMsg,
          cancelButton: false,
        },
        disableClose: true,
      })
      .afterClosed().pipe(take(1)).subscribe(() => authService.isTokenExpired.set(true));

      throw err;
    })
  )
};
