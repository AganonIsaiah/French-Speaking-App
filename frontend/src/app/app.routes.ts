import { Routes } from '@angular/router';

import { authGuard } from './guards/auth-guard';

import { Login } from './components/login/login';
import { Accueil } from './components/accueil/accueil';
import { Conversations } from './components/conversations/conversations';
import { Traduction } from './components/traduction/traduction';

export const routes: Routes = [
  {
    path: 'connexion',
    component: Login
  },
  {
    path: 'accueil',
    component: Accueil,
    canActivate: [authGuard]
  },
  {
    path: 'conversations',
    component: Conversations,
    canActivate: [authGuard]
  },
  {
    path: 'traduction',
    component: Traduction,
    canActivate: [authGuard]
  },
  {
    path: '',
    redirectTo: 'connexion',
    pathMatch: 'full'
  }
];
