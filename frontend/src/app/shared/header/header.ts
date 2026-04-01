import { Component, inject, OnInit, signal } from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { AuthService } from '../../services/auth-service';
import { CookieUtils } from '../../utils/cookie.utils';
import { UserData } from '../../models/common.model';

@Component({
  selector: 'header',
  imports: [MatMenuModule, MatButtonModule, MatIconModule],
  templateUrl: './header.html',
})
export class Header implements OnInit {
  username = signal<string>('');
  level = signal<string>('');
  menuOpen = signal<boolean>(false);

  private authService = inject(AuthService);

  ngOnInit(): void {
    const userStr = CookieUtils.get('user');
    if (userStr) {
      const userData = JSON.parse(userStr);
      this.username.set(userData.username);
      this.level.set(userData.level);
    }
  }

  onChangeLevel(newLevel: string) {
    this.level.set(newLevel);
    this.authService.updateLevel(newLevel).subscribe({
      error: (err) => console.error('Failed to update level:', err),
    });
  }

  onLogout() {
    this.authService.logout();
  }
}
