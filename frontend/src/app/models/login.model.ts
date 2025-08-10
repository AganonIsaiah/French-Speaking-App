import { UserData } from "./common.model";

export const JWT_TOKEN = 'jwt_token';
export const USER_DATA_STR = 'user';

export interface LoginData {
  username: string;
  password: string;
};

export interface LoginAPIResponse {
  token: string;
  type: string;
  id: number;
  username: string;
  email: string;
  region: string;
  level: string;
  points: number;
}

export interface SignupRequest { 
  username: string;
  email: string;
  password: string;
  region: string;
  level: string;
  points: number;
}

export interface SignupResponse {
  message: string;
  username: string;
}