import { CookieUtils } from '../utils/cookie.utils';

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
  constructor(
    public originalFrench: string,
    public translatedEnglish: string
  ) {}

  static buildModel(french: string, english: string): TradCorrigeeReqDto {
    return new TradCorrigeeReqDto(french, english);
  }
}

export class ChatReqDTO {
  constructor(
    public username: string,
    public message: string,
    public level: string
  ) {}

  static buildModel(message?: string): ChatReqDTO {
    const userStr = CookieUtils.get('user');
    const user = userStr ? JSON.parse(userStr) : { username: 'Guest', level: 'A1' };
    return new ChatReqDTO(
      user.username || 'Guest',
      message || 'Aucun message',
      user.level || 'A1'
    );
  }
}

export class ScenarioReqDTO {
  constructor(
    public username: string,
    public message: string,
    public level: string,
    public scenario: string,
    public character: string,
    public reset = false
  ) {}

  static buildModel(message: string, scenario: string, character: string, reset = false): ScenarioReqDTO {
    const userStr = CookieUtils.get('user');
    const user = userStr ? JSON.parse(userStr) : { username: 'Guest', level: 'A1' };
    return new ScenarioReqDTO(
      user.username || 'Guest',
      message,
      user.level || 'A1',
      scenario,
      character,
      reset
    );
  }
}
