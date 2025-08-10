import { UserData } from "./common.model";

export const JWT_TOKEN = 'jwt_token';
export const USER_DATA_STR = 'user_data';

export interface LoginData {
  username: string;
  password: string;
};

export interface LoginAPIResponse {
  jwt_token: string;
  message: string;
  user: UserData;
}