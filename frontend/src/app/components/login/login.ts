import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { Router } from '@angular/router';

import { AuthService } from '../../services/auth-service';
import { UserLevel } from '../../models/common.model';
import { HttpErrorResponse } from '@angular/common/http';

type AuthMethod = 'login' | 'signup';

@Component({
  selector: 'login',
  imports: [],
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

  form = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required])
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

  onSubmit() {
    const { username, password } = this.form.value;
    this.showLoginError.set('');

    this.authService.login(username, password).subscribe({
      next: () => console.log('login success'),
      error: (err) => {
        this.handleLoginError(err);
      }
    });
  }

  private handleLoginError(err: HttpErrorResponse) {
    switch (err.status) {
      case 0:
        this.showLoginError.set('A Network Error has Occurred, Try Again Later.');
        break;
      case 401 | 403: 
        this.showLoginError.set('Invalid Login Credentials.');
        break;
      default:
        this.showLoginError.set('An Error has Occurred, Try Again Later.')
    }
  }
}