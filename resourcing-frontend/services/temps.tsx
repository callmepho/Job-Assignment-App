import { AxiosResponse } from "axios";
import instance from "./axios";
import { Job } from "./jobs";
import { TokenPayload } from "./auth";

export interface Temp {
  id: number;
  firstName: string;
  lastName: string;
  jobs: Job[];
}

export interface TempDTO {
  firstName: string;
  lastName: string;
}

interface QueryParams {
  jobId: number;
}

const queryBuilder = ({ jobId }: QueryParams) => {
  const queryArray: string[] = [];
  if (jobId) {
    queryArray.push(`jobId=${jobId}`);
  }
  return "?" + queryArray.join("&");
};

export class Temps {
  public static async get(queryParams: QueryParams): Promise<Temp[]> {
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
