import { Component, signal } from '@angular/core';

import { Microphone } from './microphone/microphone';
import { TextInput } from './text-input/text-input';
import { ViewResponse } from './view-response/view-response';

@Component({
  selector: 'chatbot',
  imports: [Microphone, TextInput, ViewResponse],
  templateUrl: './chatbot.html'
})
export class Chatbot {
  readonly resList = signal<string[]>([]);

  handleRes(res: string) {
    this.resList.update(prev => [...prev, res]);
  }

}
