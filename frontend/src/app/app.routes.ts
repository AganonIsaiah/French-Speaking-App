import { Routes } from '@angular/router';

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
    component: Accueil
  },
  {
    path: 'conversations',
    component: Conversations
  },
  {
    path: 'traduction',
    component: Traduction
  },
  {
    path: '',
    redirectTo: 'connexion',
    pathMatch: 'full'
  }
];
