package manthonytat.resourcing.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import manthonytat.resourcing.enums.Role;

public class UserDTO {
  @Getter
  @Setter
  @Email
  private String email;

  @Getter
  @Setter
  private String password;

  @Getter
  @Setter
  @Enumerated(EnumType.STRING)
  private Role role;
}
