import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { Header } from './shared/header/header';
import { Sidebar } from './shared/sidebar/sidebar';
import { Chatbot } from './shared/chatbot/chatbot';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Sidebar, Chatbot],
  templateUrl: './app.html'
})
export class App {
}
