package manthonytat.resourcing.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import manthonytat.resourcing.exceptions.NotFoundException;

@Service
@Transactional
public class UserService {

  @Autowired
  private UserRepository userRepository;

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
}
