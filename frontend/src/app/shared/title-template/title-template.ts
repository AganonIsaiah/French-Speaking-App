import { Component, input } from '@angular/core';

@Component({
  selector: 'title-template',
  template: `
   <h1>{{title()}}</h1>
    <span class="flex gap-2 !mt-2">
      <p class="bg-[#008060] !px-3 rounded-xl !text-white !text-sm flex justify-center items-center">IA</p>
      <p> {{generatedType() ? generatedType() : 'Leçon générée' }}</p>
    </span>
    <hr class="!mt-4 !mb-3 border-[#e1e3e5]" />
  `
})
export class TitleTemplate {
  title = input.required<string>();
  generatedType = input<string>();
}
