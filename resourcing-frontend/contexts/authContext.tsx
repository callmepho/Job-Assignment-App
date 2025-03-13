"use client";

import React, { createContext, useEffect, useState } from "react";

export interface UserCtx {
  token: string;
  setToken: (newToken: string) => any;
}

export const AuthContext = createContext<UserCtx>({
  token: "",
  setToken: () => true,
});

export const setTokenStorage = (token: string) => {
  if (typeof window !== "undefined") {
    return window.localStorage.setItem("token", token);
  }
};

export const getTokenStorage = () => {
  if (typeof window !== "undefined") {
    console.log(window.localStorage.getItem("token"));
    return window.localStorage.getItem("token") ?? "";
  }

  return "";
};

const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [token, setToken] = useState(getTokenStorage());

  useEffect(() => {
    setTokenStorage(token);
  }, [token]);

  const authContext: UserCtx = {
    token: token,
    setToken,
  };

  return (
    <AuthContext.Provider value={authContext}>{children}</AuthContext.Provider>
  );
};

export default AuthProvider;
