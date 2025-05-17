import { Component } from '@angular/core';
import { SingleChatComponent } from '../single-chat/single-chat.component';
import { MultiChatComponent } from '../multi-chat/multi-chat.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [SingleChatComponent, MultiChatComponent, RouterModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
