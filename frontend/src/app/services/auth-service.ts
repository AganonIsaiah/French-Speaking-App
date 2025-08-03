import { Injectable, inject, signal } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private isAuthenticated = signal<boolean>(false);
  private router = inject(Router);


  login() {
    this.router.navigate(['/accueil']);

  }

  logout() {
    this.router.navigate(['/connexion']);
  }
}
