import instance from "./axios";
import { Temp, TempDTO } from "./temps";

export interface Job {
  id: number;
  name: string;
  startDate: Date;
  endDate: Date;
  temp: TempDTO;
}

export interface JobDTO {
  name: string;
  startDate: Date;
  endDate: Date;
  tempId: number;
}

interface QueryParams {
  assigned: boolean;
}

const queryBuilder = (queryParams?: QueryParams) => {
  if (!queryParams) return "";
  const queryArray: string[] = [];
  if (queryParams.assigned) {
    queryArray.push(`assigned=${queryParams.assigned}`);
  }
  return "?" + queryArray.join("&");
};

export class Jobs {
  public static async get(queryParams?: QueryParams): Promise<Job[]> {
    const queryString = queryBuilder(queryParams);
    const response = await instance.get("/jobs" + queryString);
    return response.data;
  }

  public static async getById(id: number): Promise<Job> {
    const response = await instance.get(`/jobs/${id}`);
    return response.data;
  }

  public static async patch(id: number, data: any): Promise<Job> {
    const response = await instance.patch(`/jobs/${id}`, data);
    return response.data;
  }

  public static async create(data: any): Promise<Job> {
    const response = await instance.post("/jobs", data);
    return response.data;
  }

  public static async delete(id: number): Promise<any> {
    const response = await instance.delete(`/jobs/${id}`);
    return response.data;
  }
}
