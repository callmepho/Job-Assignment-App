"use client";
import { Button } from "@/components/ui/button";
import { Auth } from "@/services/auth";

export default function Home() {
  const handleLogout = () => {
    Auth.logout();
    console.log(window.localStorage.getItem("token"));
  };
  const checkToken = () => {
    console.log(window.localStorage.getItem("token"));
  };
  return (
    <div className="grid grid-rows-[20px_1fr_20px] items-center justify-items-center min-h-screen p-8 pb-20 gap-16 sm:p-20 font-[family-name:var(--font-geist-sans)]">
      <Button onClick={handleLogout}>Logout</Button>
      <Button onClick={checkToken}>Token</Button>
    </div>
  );
}
