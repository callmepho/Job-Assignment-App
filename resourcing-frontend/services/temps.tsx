import { AxiosResponse } from "axios";
import instance from "./axios";
import { Job, JobDTO } from "./jobs";
import { TokenPayload } from "./auth";

export interface Temp {
  id: number;
  firstName: string;
  lastName: string;
  jobs: JobDTO[];
}

export interface TempDTO {
  id: number;
  firstName: string;
  lastName: string;
}

interface QueryParams {
  jobId: number;
}

const queryBuilder = (queryParams?: QueryParams) => {
  if (!queryParams) return "";
  const queryArray: string[] = [];
  if (queryParams.jobId) {
    queryArray.push(`jobId=${queryParams.jobId}`);
  }
  return "?" + queryArray.join("&");
};

export class Temps {
  public static async get(queryParams?: QueryParams): Promise<Temp[]> {
    const queryString = queryBuilder(queryParams);
    const response = await instance.get("/temps" + queryString);
    return response.data;
  }

  public static async findById(id: number): Promise<Temp> {
    const temp = await instance.get(`/temps/${id}`);
    return temp.data;
  }

  public static async patch(data: TempDTO, id: number): Promise<Temp> {
    const response = await instance.patch(`/temps/${id}`, data);
    return response.data;
  }

  public static async currentTemp(): Promise<Temp> {
    const response = await instance.get("/temps/current");
    return response.data;
  }
}
