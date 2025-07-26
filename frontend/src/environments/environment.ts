
import { EnvironmentModel } from "./environment.model";

export const environment: EnvironmentModel = {
  production: false,
  apiUrl: process.env['apiUrl'] || 'http://localhost:8080'
};
