import { Component } from '@angular/core';
import { ViewResponse } from '../../shared/chatbot/view-response/view-response';

@Component({
  selector: 'conversations',
  imports: [ViewResponse],
  template: `
  <div class="flex flex-col">
    <h1>Une conversation sans fin...</h1>
    <span class="flex gap-2 !mt-2">
      <p class="bg-[#008060] !px-3 rounded-xl !text-white !text-sm flex justify-center items-center">IA</p>
      <p>Leçon générée</p>
    </span>
    <hr class="!my-6 border-[#e1e3e5]" />
    <view-response />
  </div>
  `
})
export class Conversations { }
