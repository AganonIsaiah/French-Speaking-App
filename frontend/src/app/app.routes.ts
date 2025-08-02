import { Routes } from '@angular/router';

import { Login } from './components/login/login';
import { Accueil } from './components/accueil/accueil';
import { Conversations } from './components/conversations/conversations';

export const routes: Routes = [
  {
    path: 'accueil',
    component: Accueil
  },
  {
    path: 'connexion',
    component: Login
  },
  {
    path: 'conversations',
    component: Conversations
  },
  {
    path: '',
    redirectTo: 'accueil',
    pathMatch: 'full'
  }
];
