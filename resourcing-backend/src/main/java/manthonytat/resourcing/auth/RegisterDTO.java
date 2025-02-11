package manthonytat.resourcing.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class RegisterDTO {
  @Getter
  @Setter
  private String firstName;

  @Getter
  @Setter
  private String lastName;

  @Getter
  @Setter
  @NotBlank
  @Email
  private String email;

  @Getter
  @Setter
  @NotBlank
  private String password;
}
