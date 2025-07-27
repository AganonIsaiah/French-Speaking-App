import { Component, input } from '@angular/core';

@Component({
  selector: 'view-response',
  imports: [],
  templateUrl: './view-response.html'
})
export class ViewResponse {
  readonly resView = input<string[]>();
}
