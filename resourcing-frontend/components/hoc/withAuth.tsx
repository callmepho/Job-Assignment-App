"use client";
import { useEffect } from "react";
import { useRouter } from "next/navigation";

export function withAuth(Component: React.FC) {
  return function ProtectedComponent(props: any) {
    const router = useRouter();

    useEffect(() => {
      const token = window.localStorage.getItem("token");
      console.log(window.localStorage.getItem("token"));
      if (!token) {
        router.push("/auth/login");
      }
    }, [router]);

    return <Component {...props} />;
  };
}
