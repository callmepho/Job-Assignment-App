package manthonytat.resourcing.temp;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class TempCreateDTO {
  @Getter
  @Setter
  @NotBlank(message = "First name is required")
  private String firstName;

  @Getter
  @Setter
  @NotBlank(message = "First name is required")
  private String lastName;

}
