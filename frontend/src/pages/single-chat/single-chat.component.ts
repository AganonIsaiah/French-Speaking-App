import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ChatInputResponseComponent } from '../../components/chat-input-response/chat-input-response.component';


@Component({
  selector: 'app-single-chat',
  imports: [RouterModule, ChatInputResponseComponent],
  templateUrl: './single-chat.component.html',
  styleUrl: './single-chat.component.scss'
})
export class SingleChatComponent {

}
