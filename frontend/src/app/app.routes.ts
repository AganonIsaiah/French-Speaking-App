import { Routes } from '@angular/router';

import { LoginPage } from './pages/login-page/login-page';
import { MainPage } from './pages/main-page/main-page';
import { Conversations } from './components/conversations/conversations';

export const routes: Routes = [
  {
    path: 'accueil',
    component: MainPage
  },
  {
    path: 'connexion',
    component: LoginPage
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
