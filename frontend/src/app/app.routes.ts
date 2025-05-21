import { Routes } from '@angular/router';
import { LoginComponent } from '../features/login/login.component';
import { SignupComponent } from '../features/signup/signup.component';
import { HomeComponent } from '../features/home/home.component';
import { SingleChatComponent } from '../features/single-chat/single-chat.component';
import { MultiChatComponent } from '../features/multi-chat/multi-chat.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'signup', component: SignupComponent },
    { path: 'home', component: HomeComponent },
    { path: 'single', component: SingleChatComponent },
    { path: 'multi', component: MultiChatComponent },




    { path: '', redirectTo: 'login', pathMatch: 'full'}
];
