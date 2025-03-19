"use client";

import { withAuth } from "@/components/hoc/withAuth";
import { Job, JobDTO } from "@/services/jobs";
import { Temps } from "@/services/temps";
import { UserDetails, Users } from "@/services/user";
import React, { useEffect, useState } from "react";

const getCurrentUser = async () => {
  const userDetail = await Users.currentUser();
  const tempDetail = await Temps.currentTemp();
  return {
    id: tempDetail.id,
    email: userDetail.email,
    password: userDetail.password,
    firstName: tempDetail.firstName,
    lastName: tempDetail.lastName,
    role: userDetail.role,
    jobs: tempDetail.jobs,
  };
};

const ProfilePage = () => {
  const [currentUserDetail, setCurrentUserDetails] =
    useState<UserDetails | null>(null);

  const fetchData = async () => {
    await getCurrentUser()
      .then((data) => setCurrentUserDetails(data))
      .catch((e) => console.log(e));
    console.log(currentUserDetail);
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div>
      <h1>Profile Page</h1>
      {currentUserDetail ? (
        <div>
          <p>
            Name: {currentUserDetail.firstName} {currentUserDetail.lastName}
          </p>
          <p>Email: {currentUserDetail.email}</p>
          <p>Role: {currentUserDetail.role}</p>
        </div>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default ProfilePage;
