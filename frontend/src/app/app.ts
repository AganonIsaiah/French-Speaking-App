import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

import { Header } from './shared/header/header';
import { Sidebar } from './shared/sidebar/sidebar';
import { TextInput } from './shared/chatbot/text-input/text-input';
import { Microphone } from './shared/chatbot/microphone/microphone';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Sidebar, TextInput, Microphone, MatIconModule],
  templateUrl: './app.html'
})
export class App {
  openSidebar = signal<boolean>(true);

  toggleSidebar() {
    this.openSidebar.set(!this.openSidebar());
  }

}