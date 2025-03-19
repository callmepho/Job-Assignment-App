import instance from "./axios";
import { Job, JobDTO } from "./jobs";

export interface User {
  email: string;
  password: string;
  role: string;
}

export interface UserDetails {
  id: number;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  role: string;
  jobs: JobDTO[];
}

export class Users {
  public static async currentUser(): Promise<User> {
    const response = await instance.get("/user/current");
    return response.data;
  }
}
