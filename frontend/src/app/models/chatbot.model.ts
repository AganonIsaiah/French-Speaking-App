export interface ChatReqDTO {
  username: string;
  message: string;
  level: string;
}

export interface ChatMessage {
  sender: 'Ai' | 'Vous';
  message: string;
}