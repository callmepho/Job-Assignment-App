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
import { Job, JobDTO, Jobs } from "@/services/jobs";
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
import { Temp, Temps } from "@/services/temps";
import { Input } from "@/components/ui/input";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { Calendar } from "@/components/ui/calendar";
import { format } from "date-fns";
import { cn } from "@/lib/utils";
import {
  Drawer,
  DrawerClose,
  DrawerContent,
  DrawerDescription,
  DrawerHeader,
  DrawerTitle,
  DrawerTrigger,
} from "@/components/ui/drawer";

const JobsPage = () => {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [isModalOpen, setModalOpen] = useState(false);

  const fetchJobsData = async () => {
    await Jobs.get()
      .then((data) => setJobs(data))
      .catch((e) => console.log(e));
  };

  useEffect(() => {
    fetchJobsData();
  }, []);
  return (
    <>
      <Button onClick={() => setModalOpen(true)}>Post job</Button>
      <div className="flex flex-col my-4 gap-4 justify-center items-center">
        {jobs.map((job, idx) => (
          <JobCard data={job} key={`job${idx}`} fetchJobsData={fetchJobsData} />
        ))}
      </div>
      <Modal
        isOpen={isModalOpen}
        onClose={() => setModalOpen(false)}
        title="Create Job">
        <JobForm
          job={null}
          setModalOpen={setModalOpen}
          fetchJobsData={fetchJobsData}
        />
      </Modal>
    </>
  );
};

export default JobsPage;

interface CardData {
  data: Job;
  fetchJobsData: () => void;
}

const JobCard = ({ data, fetchJobsData }: CardData) => {
  const [isModalOpen, setModalOpen] = useState<boolean>(false);

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
        {data.temp ? (
          <div>
            <p>
              Assigned Temp: {data.temp.firstName} {data.temp.lastName}
            </p>
            <TempDrawer data={data} fetchJobsData={fetchJobsData} />
          </div>
        ) : (
          <TempDrawer data={data} fetchJobsData={fetchJobsData} />
        )}
      </CardContent>
      <Modal
        isOpen={isModalOpen}
        onClose={() => setModalOpen(false)}
        title="Editing Job">
        <JobForm
          job={data}
          setModalOpen={setModalOpen}
          fetchJobsData={fetchJobsData}
        />
      </Modal>
    </Card>
  );
};

const TempDrawer = ({ data, fetchJobsData }: CardData) => {
  const [availableTemp, setAvailableTemp] = useState<Temp[]>([]);
  const fetchTempForJob = async () => {
    await Temps.get({ jobId: data.id })
      .then((temps: Temp[]) => setAvailableTemp(temps))
      .catch((e) => console.log(e));
  };

  const assignTemp = async (tempId: number) => {
    try {
      await Jobs.patch(data.id, { tempId: tempId });
    } catch (e) {
      console.log(e);
    } finally {
      fetchJobsData();
    }
  };
  return (
    <Drawer>
      <DrawerTrigger asChild>
        <Button className="w-min" variant="outline" onClick={fetchTempForJob}>
          {data.temp ? "Edit Temp" : "Assign Temp"}
        </Button>
      </DrawerTrigger>
      <DrawerContent>
        <div className="flex flex-col justify-center items-center">
          <DrawerHeader>
            <DrawerTitle>Assign Available Temps</DrawerTitle>
            <DrawerDescription>
              List of temps you can assign for this job
            </DrawerDescription>
          </DrawerHeader>
          <DrawerClose asChild>
            {data.temp && (
              <Button
                className="w-max cursor-pointer"
                variant="destructive"
                onClick={() => assignTemp(0)}>
                Remove Temp
              </Button>
            )}
          </DrawerClose>
          {availableTemp &&
            availableTemp.map((temp, idx) => (
              <DrawerClose asChild key={`temp${idx}`}>
                <Button
                  className="w-max cursor-pointer"
                  variant="ghost"
                  onClick={() =>
                    assignTemp(temp.id)
                  }>{`${temp.firstName} ${temp.lastName}`}</Button>
              </DrawerClose>
            ))}
        </div>
      </DrawerContent>
    </Drawer>
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

const JobForm = ({
  job,
  setModalOpen,
  fetchJobsData,
}: {
  job: Job | null;
  setModalOpen: any;
  fetchJobsData: any;
}) => {
  const form = useForm<any>({
    resolver: yupResolver(jobSchema),
    defaultValues: {
      name: job?.name || "",
      startDate: job?.startDate ? new Date(job.startDate) : new Date(),
      endDate: job?.endDate ? new Date(job.endDate) : new Date(),
      temp: job?.temp || null,
    },
  });

  useEffect(() => {
    if (job) {
      form.reset({
        name: job.name,
        startDate: new Date(job.startDate),
        endDate: new Date(job.endDate),
        temp: job.temp,
      });
    }
  }, [job, form]);

  const onSubmit = async (data: JobDTO) => {
    if (job) {
      //patch job here
      await Jobs.patch(job.id, data)
        .then(() => setModalOpen(false))
        .catch((e) => console.log(e));
      fetchJobsData();
    } else {
      //create job here
      await Jobs.create(data)
        .then(() => setModalOpen(false))
        .catch((e) => console.log(e));
      fetchJobsData();
    }
  };

  const deleteJob = async () => {
    if (job) {
      await Jobs.delete(job.id)
        .then(() => setModalOpen(false))
        .catch((e) => console.log(e));
    }
  };

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
        <div className="flex w-100 justify-between">
          <Button type="submit">Submit</Button>
          {job ? (
            <Button
              variant="destructive"
              onClick={() => deleteJob()}
              type="button">
              Delete
            </Button>
          ) : null}
        </div>
      </form>
    </Form>
  );
};
