package manthonytat.resourcing.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import manthonytat.resourcing.admin.Admin;
import manthonytat.resourcing.admin.AdminRepository;
import manthonytat.resourcing.exceptions.NotFoundException;
import manthonytat.resourcing.user.User.Role;

@Service
@Transactional
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AdminRepository adminRepository;

  public User getById(Long id) {
    Optional<User> foundUser = this.userRepository.findById(id);
    if (foundUser.isPresent()) {
      return foundUser.get();
    }
    throw new NotFoundException(String.format("User with id: %d does not exist, could not find User.", id));
  }

  public User getByEmail(String email) {
    return this.userRepository.findByEmail(email).orElse(null);
  }

  public void createAdminIfNotExists() {
    Optional<User> adminExists = userRepository.findByEmail("admin@admin.com");
    if (adminExists.isEmpty()) {
      Admin admin = new Admin("init");
      admin.setEmail("admin@admin.com");
      admin.setPassword("admin123");
      admin.setRole(Role.ADMIN);
      this.adminRepository.save(admin);
      System.out.println("Admin user created successfully!");
    } else {
      System.out.println("Admin user already exists.");
    }
  }

  public long getUserCount() {
    return this.userRepository.count();
  }
}
