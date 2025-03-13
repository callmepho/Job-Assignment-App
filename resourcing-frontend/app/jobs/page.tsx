"use client";
import Modal from "@/components/Modal";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Job, Jobs } from "@/services/jobs";
import { CalendarIcon, SquarePen } from "lucide-react";
import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import * as Yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Temp } from "@/services/temps";
import { Input } from "@/components/ui/input";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { Calendar } from "@/components/ui/calendar";
import { format } from "date-fns";
import { cn } from "@/lib/utils";

const JobsPage = () => {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [isModalOpen, setModalOpen] = useState(false);

  const fetchJobsData = async () => {
    await Jobs.get()
      .then((data) => setJobs(data))
      .catch((e) => console.log);
  };

  useEffect(() => {
    fetchJobsData();
  }, []);
  return (
    <>
      <Button onClick={() => setModalOpen(true)}>Create new job</Button>
      <div className="flex flex-col my-4 gap-4 justify-center items-center">
        {jobs.map((job, idx) => (
          <JobCard data={job} key={`job${idx}`} />
        ))}
      </div>
      <Modal
        isOpen={isModalOpen}
        onClose={() => setModalOpen(false)}
        title="Create Job">
        <JobCreateForm />
      </Modal>
    </>
  );
};

export default JobsPage;

interface CardData {
  data: Job;
}

const JobCard = ({ data }: CardData) => {
  const [isModalOpen, setModalOpen] = useState(false);
  return (
    <Card className="w-xl h-min">
      <CardHeader>
        <CardTitle className="flex justify-between">
          {data.name}
          <SquarePen
            className="cursor-pointer"
            onClick={() => setModalOpen(true)}
          />
        </CardTitle>
        <CardDescription>
          Start Date: {new Date(data.startDate).toDateString()} End Date:{" "}
          {new Date(data.endDate).toDateString()}
        </CardDescription>
      </CardHeader>
      <CardContent>
        Assigned Temp:{" "}
        {data.temp ? (
          `${data.temp.firstName} ${data.temp.lastName}`
        ) : (
          <span className="italic font-thin">none</span>
        )}
      </CardContent>
      <Modal
        isOpen={isModalOpen}
        onClose={() => setModalOpen(false)}
        title="Editing Job">
        <p>This is a reusable modal component.</p>
      </Modal>
    </Card>
  );
};

const jobSchema: Yup.ObjectSchema<any> = Yup.object().shape({
  name: Yup.string().required("Name is required"),
  startDate: Yup.date().required("Start date is required"),
  endDate: Yup.date()
    .required("End date is required")
    .min(Yup.ref("startDate"), "End date must be after start date"),
  temp: Yup.mixed<Temp>().nullable(),
});

const JobCreateForm = () => {
  const form = useForm<any>({
    resolver: yupResolver(jobSchema),
    defaultValues: {
      name: "",
      startDate: new Date(),
      endDate: new Date(),
      temp: null,
    },
  });

  const onSubmit = () => {};

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="w-2/3 space-y-6">
        <FormField
          control={form.control}
          name="name"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Name:</FormLabel>
              <FormControl>
                <Input placeholder="Enter job name" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="startDate"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel>Start Date:</FormLabel>
              <Popover>
                <PopoverTrigger asChild>
                  <FormControl>
                    <Button
                      variant={"outline"}
                      className={cn(
                        "w-[240px] pl-3 text-left font-normal",
                        !field.value && "text-muted-foreground"
                      )}>
                      {field.value ? (
                        format(field.value, "PPP")
                      ) : (
                        <span>Pick a date</span>
                      )}
                      <CalendarIcon className="ml-auto h-4 w-4 opacity-50" />
                    </Button>
                  </FormControl>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0" align="start">
                  <Calendar
                    mode="single"
                    selected={field.value}
                    onSelect={field.onChange}
                    disabled={(date) =>
                      date > new Date() || date < new Date("1900-01-01")
                    }
                    initialFocus
                  />
                </PopoverContent>
              </Popover>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="endDate"
          render={({ field }) => (
            <FormItem className="flex flex-col">
              <FormLabel>End Date:</FormLabel>
              <Popover>
                <PopoverTrigger asChild>
                  <FormControl>
                    <Button
                      variant={"outline"}
                      className={cn(
                        "w-[240px] pl-3 text-left font-normal",
                        !field.value && "text-muted-foreground"
                      )}>
                      {field.value ? (
                        format(field.value, "PPP")
                      ) : (
                        <span>Pick a date</span>
                      )}
                      <CalendarIcon className="ml-auto h-4 w-4 opacity-50" />
                    </Button>
                  </FormControl>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0" align="start">
                  <Calendar
                    mode="single"
                    selected={field.value}
                    onSelect={field.onChange}
                    disabled={(date) =>
                      date > new Date() || date < new Date("1900-01-01")
                    }
                    initialFocus
                  />
                </PopoverContent>
              </Popover>
              <FormMessage />
            </FormItem>
          )}
        />

        <Button type="submit">Submit</Button>
      </form>
    </Form>
  );
};
