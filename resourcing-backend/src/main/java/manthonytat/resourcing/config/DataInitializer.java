package manthonytat.resourcing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import manthonytat.resourcing.job.JobService;
import manthonytat.resourcing.temp.TempService;
import manthonytat.resourcing.user.UserService;

@Component
public class DataInitializer implements CommandLineRunner {
  @Autowired
  private UserService userService;

  @Autowired
  private TempService tempService;

  @Autowired
  private JobService jobService;

  @Override
  public void run(String... args) {
    System.out.println("Checking for admin user...");
    this.userService.createAdminIfNotExists();
    System.out.println("Deleting old fake temps...");
    this.tempService.deleteFakeJobs();
    System.out.println("Generating fake temps...");
    this.tempService.createFakeUsers(15);
    System.out.println("Deleting old fake jobs...");
    this.jobService.deleteFakeJobs();
    System.out.println("Generating new fake jobs...");
    this.jobService.createFakeJobs(10);
    System.out.println("Initial fake data generation completed!!!");
  }
}
