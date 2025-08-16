import { Component, input, signal } from '@angular/core';

import { MatProgressBarModule } from '@angular/material/progress-bar';

@Component({
  selector: 'title-template',
  imports: [MatProgressBarModule],
  template: `
   <h1 class="flex justify-center !font-semibold">{{title()}}</h1>
  
    <span class="flex justify-center gap-4 !mt-4">
      <p class="bg-[#008060] !px-3 rounded-xl !text-white !text-sm flex justify-center items-center">IA</p>
      <p> {{generatedType() ? generatedType() : 'Leçon générée' }}</p>    
      @if (points() && points()! >= 0) {
      <span class=" flex items-center my-4 w-[30%] min-w-70 gap-2">
        <mat-progress-bar class="progress-bar" mode="determinate" [value]="points()"></mat-progress-bar>
        <p class="whitespace-nowrap">{{ points() }}%</p>
      </span>
      }  
    </span>
    <hr class="!mt-5 !mb-3" />
  `
})
export class TitleTemplate {
  title = input.required<string>();
  generatedType = input<string>();
  points = input<number>();
}
