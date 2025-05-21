import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ChatInputResponseComponent } from '../../shared/components/chat-input-response/chat-input-response.component';
import { ScreenComponent } from '../../shared/components/screen/screen.component';
import { SidebarComponent } from '../../shared/components/sidebar/sidebar.component';
 

@Component({
  selector: 'app-single-chat',
  imports: [RouterModule, ChatInputResponseComponent, ScreenComponent, SidebarComponent],
  templateUrl: './single-chat.component.html',
  styleUrl: './single-chat.component.scss'
})
export class SingleChatComponent {
  pageTitle = 'Chat Simple';
  modes = ['Conversationnel', 'L\'enseignement', 'Compétitif' ]
}
