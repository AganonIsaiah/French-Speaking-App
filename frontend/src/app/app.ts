import { Component, signal, OnInit, inject, DestroyRef } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { RouterOutlet } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

import { Header } from './shared/header/header';
import { Sidebar } from './shared/sidebar/sidebar';
import { Login } from './components/login/login';

import { TTSService } from './services/tts-service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Header, Sidebar, MatIconModule, Login],
  templateUrl: './app.html'
})
export class App implements OnInit {
  showLogin = signal<boolean>(true);

  private router = inject(Router);
  private destroyRef = inject(DestroyRef);
  private ttsService = inject(TTSService);

  ngOnInit() {
    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe((event: NavigationEnd) => {
        this.ttsService.cancel();
        const currentUrl = event.urlAfterRedirects;
        this.showLogin.set(currentUrl === '/connexion');
      });

    window.addEventListener('beforeunload', () => {
      this.ttsService.cancel();
    })
  }
}