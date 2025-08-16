import { Component, inject, OnInit, signal } from '@angular/core';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { AuthService } from '../../services/auth-service';

@Component({

  selector: 'header',
  imports: [MatProgressBarModule, MatMenuModule, MatButtonModule, MatIconModule],
  templateUrl: './header.html'
})
export class Header implements OnInit {
  username = signal<string>('');
  level = signal<string>('');
  menuOpen = signal<boolean>(false);

  private authService = inject(AuthService);

  ngOnInit(): void {
    const userDataStr = localStorage.getItem('user');

    if (userDataStr) {
      const userData = JSON.parse(userDataStr);
      this.username.set(userData.username);
      this.level.set(userData.level);
    }
  }

  onChangeLevel(newLevel: string) {
    this.level.set(newLevel);

    this.authService.updateLevel(newLevel).subscribe({
      next: (response) => console.log('Level updated successfully:', response),
      error: (err) => console.error('Failed to update level:', err),
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
