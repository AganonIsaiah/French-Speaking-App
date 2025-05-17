import { Routes } from '@angular/router';
import { LoginComponent } from '../pages/login/login.component';
import { SignupComponent } from '../pages/signup/signup.component';
import { HomeComponent } from '../pages/home/home.component';
import { SingleChatComponent } from '../pages/single-chat/single-chat.component';
import { MultiChatComponent } from '../pages/multi-chat/multi-chat.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'signup', component: SignupComponent },
    { path: 'home', component: HomeComponent },
    { path: 'single', component: SingleChatComponent },
    { path: 'multi', component: MultiChatComponent },




    { path: '', redirectTo: 'login', pathMatch: 'full'}
];
