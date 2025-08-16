export interface EnvironmentModel {
  production: boolean;
  apiUrl: string;
}

export enum ApiEndpoint {
  LOGIN =  'api/auth/login',
  SIGNUP = 'api/auth/signup',
  CONVERSATIONS = 'api/chat/conversations',
  DIXPHRASES = 'api/chat/dix-phrases',
  TRADUCTIONSRAPIDESCORRIGEES = 'api/chat/traduction-rapides'
}
         