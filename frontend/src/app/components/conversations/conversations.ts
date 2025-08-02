import { Component } from '@angular/core';
import { ViewResponse } from '../../shared/chatbot/view-response/view-response';
import { TitleTemplate } from '../title-template/title-template';

@Component({
  selector: 'conversations',
  imports: [ViewResponse, TitleTemplate],
  template: `
  <div class="flex flex-col">
    <title-template [title]="'Une conversation sans fin...'"/>
    <view-response />
  </div>
  `
})
export class Conversations { }
