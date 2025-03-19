"use client";
import { withAuth } from "@/components/hoc/withAuth";
import { Card, CardHeader, CardTitle } from "@/components/ui/card";
import { Temp, Temps } from "@/services/temps";
import React, { useEffect, useState } from "react";

const TempsPage = () => {
  const [temps, setTemps] = useState<Temp[]>([]);
  const fetchTempsData = async () => {
    await Temps.get()
      .then((data) => setTemps(data))
      .catch((e) => console.log(e));
  };

  useEffect(() => {
    fetchTempsData();
  }, []);

  return (
    <div>
      {temps.map((temp, idx) => (
        <TempCard temp={temp} key={`temp${idx}`} />
      ))}
    </div>
  );
};

export default TempsPage;

interface CardData {
  temp: Temp;
}

const TempCard = ({ temp }: CardData) => {
  return (
    <Card>
      <CardHeader>
        <CardTitle>{temp.firstName + " " + temp.lastName}</CardTitle>
      </CardHeader>
    </Card>
  );
};
