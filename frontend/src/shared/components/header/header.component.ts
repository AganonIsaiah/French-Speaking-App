import { NgFor } from '@angular/common';
import { Component, Input } from '@angular/core';
import { AuthService } from '../../../core/services/auth/auth.service';
import { UserDisplay } from '../../../core/models/user-display';

@Component({
  selector: 'app-header',
  imports: [NgFor],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  @Input() title!: string;
  user!: UserDisplay;

  userInfoList = [
    () => `Bienvenue, ${this.user?.username}!`,
    () => `Points: ${this.user?.points}`,
    () => `Région: ${this.user?.region}`,
    () => `Compétence: ${this.user?.proficiency}`
  ];


  constructor(private authService: AuthService) { }


  ngOnInit(): void {
    this.user = this.authService.getUser();
  }
}
