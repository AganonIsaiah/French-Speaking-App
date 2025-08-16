export interface EnvironmentModel {
  production: boolean;
  apiUrl: string;
}

export enum ApiEndpoint {
  LOGIN =  'api/auth/login',
  SIGNUP = 'api/auth/signup',
  CONVERSATIONS = 'api/chat/conversations',
  TRADUCTIONRAPIDES = 'api/chat/traduction-rapides'
}
         