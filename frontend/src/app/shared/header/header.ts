import { Component, inject } from '@angular/core';
import { MatProgressBarModule } from '@angular/material/progress-bar';

import { AuthService } from '../../services/auth-service';
@Component({

  selector: 'header',
  imports: [MatProgressBarModule],
  templateUrl: './header.html'
})
export class Header {
  username = 'isaiah';
  level = 'Niveau B2';
  points = 70;

  private authService = inject(AuthService);

  onLogout() {
    this.authService.logout();
  }
}
