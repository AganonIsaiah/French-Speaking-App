import { Component, Injectable, inject } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Microphone } from './microphone/microphone';
import { TextInput } from './text-input/text-input';
import { ViewResponse } from './view-response/view-response';
import { ChatbotService } from '../../services/chatbot-service';

@Component({
  selector: 'chatbot',
  imports: [Microphone, TextInput, ViewResponse, MatIconModule],
  templateUrl: './chatbot.html',
  styleUrls: ['./chatbot.scss']
})
export class Chatbot {

}
