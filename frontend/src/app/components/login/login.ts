import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { Router } from '@angular/router';

import { AuthService } from '../../services/auth-service';
import { UserLevel } from '../../models/common.model';
import { HttpErrorResponse } from '@angular/common/http';

type AuthMethod = 'login' | 'signup';

@Component({
  selector: 'login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html'
})
export class Login {
  private router = inject(Router);
  private authService = inject(AuthService);
  readonly levels: UserLevel[] = ['A1', 'A2', 'B1', 'B2', 'C1', 'C2'];

  loginType = signal<AuthMethod>('login');
  levelType = signal<UserLevel>('A1');
  showPassword = signal<boolean>(false);
  showLoginError = signal<string>('');
  showSignupMsg = signal<string>('');

  form = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    region: new FormControl('', [Validators.required])
  })

  onToggleAuthMethod(type: AuthMethod) {
    this.loginType.set(type);
  }

  onToggleLevel(type: UserLevel) {
    this.levelType.set(type);
  }

  isLevelSelected(type: UserLevel) {
    return this.levelType() === type;
  }

  onShowPassword() {
    this.showPassword.set(!this.showPassword())
  }

  onSignup() {
    const { username, password, email, region } = this.form.value;
    const level = this.levelType();

    if (!username || !password || !email || !region) {
      this.showLoginError.set('Veuillez remplir tous les champs obligatoires.');
      return;
    }

    this.authService.signup(username, email, region, level, password).subscribe({
      next: (response) => {
        console.log('Signup successful:', response);
        this.loginType.set('login');
        this.showSignupMsg.set('Inscription réussie!');
      },
      error: (error) => {
        this.handleLoginError(error);
      },
      complete: () => {
        this.form.get('password')?.reset();
        this.form.get('email')?.reset();
        this.form.get('region')?.reset();
        this.levelType.set('A1');
      }
    });


  }

  onLogin() {
    const { username, password } = this.form.value;

    console.log(`Login: ${username}, ${password}`)
    this.showLoginError.set('');

    this.authService.login(username, password).subscribe({
      next: () => console.log('login success'),
      error: (err) => this.handleLoginError(err),
      complete: () => this.form.reset()
    });
  }

  private handleLoginError(err: HttpErrorResponse) {

    switch (err.status) {
      case 0:
        this.showLoginError.set('A Network Error has Occurred, Try Again Later.');
        break;
      case 400:
      case 401:
      case 403:
        this.showLoginError.set('Invalid Login Credentials.');
        break;
      default:
        this.showLoginError.set('An Error has Occurred, Try Again Later.')
    }
  }
}