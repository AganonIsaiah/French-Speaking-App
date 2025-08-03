import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { AuthService } from '../../services/auth-service';

type AuthMethod = 'login' | 'signup';
type UserLevel = 'A1' | 'A2' | 'B1' | 'B2' | 'C1' | 'C2' ;

@Component({
  selector: 'login',
  imports: [],
  templateUrl: './login.html'
})
export class Login {
  loginType = signal<AuthMethod>('login');
  levelType = signal<UserLevel>('A1');
  showPassword = signal<boolean>(false);

  form = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required])
  })

  private authService = inject(AuthService);

  onToggleAuthMethod (type: AuthMethod ) {
    this.loginType.set(type);
  }

  onToggleLevel (type: any) {
    this.levelType.set(type);
  }

  isLevelSelected (type: any) {
    return this.levelType() === type;
  }

  onLogin() {
    this.authService.login();
  }
}
