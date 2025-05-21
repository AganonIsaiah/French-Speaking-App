import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';


// Components
import { HomeComponent } from '../features/home/home.component';
import { LoginComponent } from '../features/login/login.component';
import { SignupComponent } from '../features/signup/signup.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HomeComponent, LoginComponent, SignupComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'angular-frontend';
}
