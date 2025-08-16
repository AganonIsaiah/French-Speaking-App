export type UserLevel = 'A1' | 'A2' | 'B1' | 'B2' | 'C1' | 'C2';

export interface UserData {
  username: string;
  email: string;
  region?: string;
  level?: string;
}