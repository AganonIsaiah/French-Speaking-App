import { InjectionToken } from "@angular/core";
import { EnvironmentModel } from "./environment.model";

export const ENVIRONMENT_TOKEN = new InjectionToken<EnvironmentModel>('ENVIRONMENT_TOKEN')