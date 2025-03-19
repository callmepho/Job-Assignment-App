package manthonytat.resourcing.user;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  // @Autowired
  // private UserService userService;

  @GetMapping("/current")
  public ResponseEntity<UserDTO> getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) auth.getPrincipal();
    UserDTO current = new UserDTO();
    current.setEmail(user.getEmail());
    current.setPassword(user.getPassword());
    current.setRole(user.getRole());
    return new ResponseEntity<UserDTO>(current, HttpStatus.OK);
  }
}
