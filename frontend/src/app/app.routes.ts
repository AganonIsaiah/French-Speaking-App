import { Routes } from '@angular/router';

import { LoginPage } from './components/login-page/login-page';
import { MainPage } from './components/main-page/main-page';

export const routes: Routes = [
  {
    path: 'main',
    component: MainPage
  },
  {
    path: 'login',
    component: LoginPage
  },
  {
    path: '',
    redirectTo: 'main',
    pathMatch: 'full'
  }
];
