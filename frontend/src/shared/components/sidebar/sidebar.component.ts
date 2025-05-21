import { NgFor } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { AuthService } from '../../../core/services/auth/auth.service';
import { UserDisplay } from '../../../core/models/user-display';

@Component({
  selector: 'app-sidebar',
  imports: [NgFor],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  @Input() title!: string;
  @Input() links: string[] = [];

  selectedLink: string = '';
  user!: UserDisplay;


  constructor(private authService: AuthService) {}


  ngOnInit(): void {
      this.user = this.authService.getUser();
  }

  onLinkClick(link: string) {
    this.selectedLink = link;
    console.log('Selected:', link);
  }
}
