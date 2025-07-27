import { Component, inject, output } from '@angular/core';
import { FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ChatbotService } from '../../../services/chatbot-service';
import { ChatReqDTO } from '../../../services/chatbot.model';

@Component({
  selector: 'text-input',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule 
  ],
  templateUrl: './text-input.html'
})
export class TextInput {
  private chatbotService = inject(ChatbotService);
  msgControl = new FormControl('', Validators.required);
  readonly res = output<string>();



  onSubmitMsg(): void {
    const msg = this.msgControl.value;
    if (!msg) return;

    const req : ChatReqDTO = {
      username: 'Isaiah',
      message: msg,
      level: 'A2',
    }

    this.chatbotService.generateChat(req).subscribe({
      next: (res: string) => {
        this.res.emit(res);
        this.msgControl.reset();
        console.log('Response:', res);
      },
      error: (err) => console.error('Error:', err)
    });
  }
}
