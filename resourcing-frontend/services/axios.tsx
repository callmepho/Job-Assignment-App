import axios from "axios";

const instance = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 1000,
  withCredentials: false,
});

instance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && !window.localStorage.getItem("token")) {
      window.location.href = "/auth/login";
    } else if (error.response && error.response.status === 401) {
      window.location.href = "/error/401";
    } else {
      return Promise.reject(error);
    }
  }
);

instance.interceptors.request.use(
  (config) => {
    const token = window.localStorage.getItem("token");
    console.log(token);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default instance;
