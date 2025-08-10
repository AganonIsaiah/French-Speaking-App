import {UserLevel } from "./common.model";

export interface ChatReqDTO {
  username: string;
  message: string;
  level: UserLevel;
}

export interface ChatMessage {
  sender: 'Ai' | 'Vous';
  message: string;
}