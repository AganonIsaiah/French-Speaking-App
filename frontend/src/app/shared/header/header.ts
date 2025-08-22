import { Component, inject, OnInit, signal } from '@angular/core';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { AuthService } from '../../services/auth-service';
import { USER_DATA_STR } from '../../models/login.model';
import { UserData } from '../../models/common.model';

@Component({

  selector: 'header',
  imports: [MatProgressBarModule, MatMenuModule, MatButtonModule, MatIconModule],
  templateUrl: './header.html'
})
export class Header implements OnInit {
  username = signal<string>('');
  level = signal<string>('');
  email = signal<string>('');
  region = signal<string>('');
  menuOpen = signal<boolean>(false);

  private authService = inject(AuthService);

  ngOnInit(): void {
    const userDataDisplay = localStorage.getItem(USER_DATA_STR);

    if (userDataDisplay) {
      const userData = JSON.parse(userDataDisplay);
      this.username.set(userData.username);
      this.level.set(userData.level);
      this.email.set(userData.email);
      this.region.set(userData.region);
    }
  }

  onChangeLevel(newLevel: string) {
    this.level.set(newLevel);

    this.authService.updateLevel(newLevel).subscribe({
      next: (response) => console.log('Level updated successfully:', response),
      error: (err) => console.error('Failed to update level:', err),
      complete: () => {
        const userDataStr = localStorage.getItem('user');

        if (userDataStr) {
          const userData: UserData = {
            username: this.username(),
            email: this.email(),
            region: this.region(),
            level: this.level()
          }

          localStorage.setItem(USER_DATA_STR, JSON.stringify(userData));
        }
      }
    });
  }

  onMenuOpen() {
    this.menuOpen.set(true);
  }

  onMenuClose() {
    this.menuOpen.set(false);
  }

  onLogout() {
    this.authService.logout();
  }
}
