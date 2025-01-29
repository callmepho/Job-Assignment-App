import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.tsx";
import { BrowserRouter, Route, Routes } from "react-router";
import { Login } from "./pages/Account/Login.tsx";
import { JobsHome } from "./pages/Jobs/JobsHome.tsx";
import { TempsHome } from "./pages/Temps/TempsHome.tsx";
import { Profile } from "./pages/Profile/Profile.tsx";
import { Job } from "./pages/Jobs/Job.tsx";
import { JobsLayout } from "./pages/Jobs/JobsLayout.tsx";
import { EditJob } from "./pages/Jobs/EditJob.tsx";
import { Temp } from "./pages/Temps/Temp.tsx";
import { EditTemp } from "./pages/Temps/EditTemp.tsx";
import { TempsLayout } from "./pages/Temps/TempsLayout.tsx";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route index element={<App />} />
        <Route path="login" element={<Login />} />
        <Route path="jobs">
          <Route index element={<JobsHome />} />
          <Route element={<JobsLayout />}>
            <Route path=":id" element={<Job />} />
            <Route path=":id/edit" element={<EditJob />} />
          </Route>
        </Route>
        <Route path="temps">
          <Route index element={<TempsHome />} />
          <Route element={<TempsLayout />}>
            <Route path=":id" element={<Temp />} />
            <Route path=":id/edit" element={<EditTemp />} />
          </Route>
        </Route>
        <Route path="profile" element={<Profile />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>
);
