export interface EnvironmentModel {
  production: boolean;
  apiUrl: string;
}

export enum ApiEndpoint {
  CHAT = 'api/chat/gemini',
  ASSIST = 'api/chat/assist',
  LOGIN =  'api/auth/login',
  SIGNUP = 'api/auth/signup'
}