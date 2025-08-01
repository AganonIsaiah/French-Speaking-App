import { Component, input, inject, Injectable } from '@angular/core';
import { ChatbotService } from '../../../services/chatbot-service';

@Component({
  selector: 'view-response',
  imports: [],
  templateUrl: './view-response.html'
})
export class ViewResponse {
  public chatbotService = inject(ChatbotService);
}
