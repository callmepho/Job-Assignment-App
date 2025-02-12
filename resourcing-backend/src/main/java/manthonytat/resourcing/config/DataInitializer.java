package manthonytat.resourcing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import manthonytat.resourcing.temp.TempService;
import manthonytat.resourcing.user.UserService;

@Component
public class DataInitializer implements CommandLineRunner {
  @Autowired
  private UserService userService;

  @Autowired
  private TempService tempService;

  @Override
  public void run(String... args) {
    System.out.println("Checking for admin user...");
    this.userService.createAdminIfNotExists();
    System.out.println("Checking number of user is above 50...");
    long numUsers = this.userService.getUserCount();
    if (numUsers < 50) {
      System.out.println("Number of users: " + numUsers);
      System.out.println("Generating " + (50 - numUsers) + " more temp users...");
      this.tempService.createFakeUsers(50 - numUsers);
      System.out.println("Generated temp users successfully");
    } else {
      System.out.println("Number of users: " + numUsers);
    }
  }
}
