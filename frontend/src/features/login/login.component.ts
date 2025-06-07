import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth/auth.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  loginData = {
    username: '',
    password: ''
  }

  constructor(private router: Router, private authService: AuthService) { }

  onLogin() {
    this.authService.login(this.loginData).subscribe({
      next: (res) => {
      
        if (res.success) {
          this.authService.saveUser(res.user);
          this.router.navigate(['/home']);
        } else {
          console.error('Login failed:', res.message);
        }
      },
      error: err => {
        console.error('Login error:', err);
      }
    })

  }
}
