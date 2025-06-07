import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ChatInputResponseComponent } from '../../shared/components/chat-input-response/chat-input-response.component';
import { HeaderComponent } from '../../shared/components/header/header.component';
import { DockComponent } from '../../shared/components/dock/dock.component';
 

@Component({
  selector: 'app-single-chat',
  imports: [RouterModule, ChatInputResponseComponent, HeaderComponent, DockComponent],
  templateUrl: './single-chat.component.html',
  styleUrl: './single-chat.component.scss'
})
export class SingleChatComponent {
  pageTitle = 'Chat Simple';
}
