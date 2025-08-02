import { Component, signal, OnInit, inject, DestroyRef } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { RouterOutlet } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

import { Header } from './shared/header/header';
import { Sidebar } from './shared/sidebar/sidebar';
import { TextInput } from './shared/chatbot/text-input/text-input';
import { Microphone } from './shared/chatbot/microphone/microphone';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Header, Sidebar, TextInput, Microphone, MatIconModule],
  templateUrl: './app.html'
})
export class App implements OnInit {
  openSidebar = signal<boolean>(true);
  showMicTextInput = signal<boolean>(true);

  private router = inject(Router);
  private destroyRef = inject(DestroyRef);

  ngOnInit() {
    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe((event: NavigationEnd) => {
        const currentUrl = event.urlAfterRedirects;
        this.showMicTextInput.set(currentUrl !== '/traduction');
      });
  }

  toggleSidebar() {
    this.openSidebar.set(!this.openSidebar());
  }
}
