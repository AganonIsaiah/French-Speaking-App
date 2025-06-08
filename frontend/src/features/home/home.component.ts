import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth/auth.service';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [ RouterModule, NgFor],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  user: any;
  singleChat = ['Conversationnel', 'Enseignement'];
  multiChat = ['Informel','Privé','Compétitif']


  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
    this.user = this.authService.getUser();
  }

  navToSingle(event: any) {
    const mode = event.target.value;
    this.router.navigate(['/single'], { queryParams: { mode } });
  }

  navToMulti(event: any) {
    const mode = event.target.value;
    this.router.navigate(['/multi'], { queryParams: { mode }});
  }
}
