import { Component, inject, OnInit } from '@angular/core';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { AuthService } from '../../services/auth-service';

@Component({

  selector: 'header',
  imports: [MatProgressBarModule],
  templateUrl: './header.html'
})
export class Header implements OnInit {
  userData: any;
  username: string = '';
  level: string = ''; 

  private authService = inject(AuthService);

  ngOnInit(): void {
    const userData = localStorage.getItem('user');
    if (userData) {
      this.userData = JSON.parse(userData);
      this.username = this.userData.username;
      this.level = this.userData.level; 
    }
  }

  onLogout() {
    this.authService.logout();
  }
}
