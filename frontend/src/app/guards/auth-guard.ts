import { CanActivateFn, Router, UrlTree, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { inject } from '@angular/core';

import { Observable } from 'rxjs';

import { AuthService } from '../services/auth-service';

export const authGuard: CanActivateFn = (
  route, 
  state):
  Observable<boolean | UrlTree>
  | Promise<boolean | UrlTree>
  | boolean 
  | UrlTree => {
  return inject(AuthService).isAuthenticated() ? true : inject(Router).createUrlTree(['/accueil'])
};
