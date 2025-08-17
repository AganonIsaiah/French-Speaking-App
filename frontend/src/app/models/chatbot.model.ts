export interface ChatMessage {
  sender: 'Ai' | 'Vous' | 'gen';
  message?: string;
  points?: number; 
}

export interface TradRapidesResult {
  points: number;
  feedback: string;
}

export class TradCorrigeeReqDto {
  constructor (
    public originalFrench: string,
    public translatedEnglish: string
  ) {}

  static buildModel(french: string, english: string): TradCorrigeeReqDto {
    return new TradCorrigeeReqDto(
      french,
      english
    );
  }
}

export class ChatReqDTO {
  constructor(
    public username: string,
    public message: string,
    public level: string
  ) {}

  static buildModel(message?: string): ChatReqDTO {
    const userString = localStorage.getItem('user');
    const user = userString
      ? JSON.parse(userString)
      : { username: 'Guest', level: 'A1' };

    return new ChatReqDTO(
      user.username || 'Guest',
      message || 'Aucun message',
      user.level || 'A1'
    );
  }
}