import { Component, inject, OnInit } from '@angular/core';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { AuthService } from '../../services/auth-service';

import { UserData } from '../../models/common.model';

@Component({

  selector: 'header',
  imports: [MatProgressBarModule],
  templateUrl: './header.html'
})
export class Header implements OnInit {
  userData: any;
  username: string = '';
  level: string = '';
  points: number = 0;

  private authService = inject(AuthService);

  ngOnInit(): void {
    const userData = localStorage.getItem('user');
    if (userData) {
      this.userData = JSON.parse(userData);
      this.username = this.userData.username;
      this.level = this.userData.level;
      this.points = this.userData.points;
    }
  }

  onLogout() {
    this.authService.logout();
  }
}
