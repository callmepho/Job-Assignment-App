import { AxiosResponse } from "axios";
import instance from "./axios";

export interface LoginPayload {
  email: string;
  password: string;
}

export interface TokenPayload {
  token: string;
}

export interface RegisterPayLoad {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

export class Auth {
  public static async login(
    loginPayload: LoginPayload
  ): Promise<AxiosResponse<TokenPayload>> {
    const response = await instance.post("/auth/login", loginPayload);
    return response;
  }

  public static async register(
    registerPayLoad: RegisterPayLoad
  ): Promise<AxiosResponse<TokenPayload>> {
    const data = await instance.post("/auth/register", registerPayLoad);
    return data;
  }

  public static logout() {
    if (typeof window !== "undefined") {
      window.localStorage.removeItem("token");
    }
  }
}
