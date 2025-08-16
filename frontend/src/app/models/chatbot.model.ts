export interface ChatMessage {
  sender: 'Ai' | 'Vous';
  message: string;
}

export class ChatReqDTO {
  constructor(
    public username: string,
    public message: string,
    public level: string
  ) {}

  static buildModel(message: string): ChatReqDTO {
    const userString = localStorage.getItem('user');
    const user = userString
      ? JSON.parse(userString)
      : { username: 'Guest', level: 'A1' };

    return new ChatReqDTO(
      user.username || 'Guest',
      message,
      user.level || 'A1'
    );
  }
}