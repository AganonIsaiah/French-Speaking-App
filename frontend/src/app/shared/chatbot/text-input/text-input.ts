import { Component, inject, signal, ElementRef, HostListener } from '@angular/core';
import { FormControl, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ChatbotService } from '../../../services/chatbot-service';
import { ChatReqDTO } from '../../../models/chatbot.model';

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
  private eltRef = inject(ElementRef);

  msgControl = new FormControl('', Validators.required);
  openSuggestions = signal<boolean>(false);
  suggestions = signal<string[]>(["suggest 1", "suggest 2", "suggest 3"])

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    if (!this.eltRef.nativeElement.contains(event.target as Node)) this.openSuggestions.set(false);
  }

  selectSuggestion(suggestion: string) {
    this.msgControl.setValue(suggestion);
    this.openSuggestions.set(false);
  }


  onSubmitMsg() {
    const msg = this.msgControl.value;
    if (!msg) return;

    const userString = localStorage.getItem('user');
    const user = userString ? JSON.parse(userString) : { username: 'Guest', level: 'A1' };

    const userModel: ChatReqDTO = {
      username: user.username || 'Guest',
      message: msg,
      level: user.level || 'A1'
    }
    this.chatbotService.generateChat(userModel).subscribe({
      next: (res: string) => {
        this.msgControl.reset();
        console.log('Response:', res);
      },
      error: (err) => console.error('Error:', err)
    });
  }
}
