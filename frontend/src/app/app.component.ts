import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';


// Components
import { HomeComponent } from '../pages/home/home.component';
import { LoginComponent } from '../pages/login/login.component';
import { SignupComponent } from '../pages/signup/signup.component';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, HomeComponent, LoginComponent, SignupComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'angular-frontend';
}
