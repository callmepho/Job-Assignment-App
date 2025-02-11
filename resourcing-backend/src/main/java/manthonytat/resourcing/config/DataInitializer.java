package manthonytat.resourcing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import manthonytat.resourcing.user.UserService;

@Component
public class DataInitializer implements CommandLineRunner {
  @Autowired
  private UserService userService;

  @Override
  public void run(String... args) {
    System.out.println("Checking for admin user...");
    this.userService.createAdminIfNotExists();
  }
}
