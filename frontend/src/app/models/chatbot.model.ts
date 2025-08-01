export type Level = 'A1' | 'A2' | 'B1' | 'B2' | 'C1' | 'C2';

export interface ChatReqDTO {
  username: string;
  message: string;
  level: Level;
}