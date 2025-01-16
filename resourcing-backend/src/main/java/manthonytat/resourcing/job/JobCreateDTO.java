package manthonytat.resourcing.job;

import java.util.Date;

import org.springframework.lang.Nullable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class JobCreateDTO {
  @Getter
  @Setter
  @NotNull
  @NotBlank
  private String name;

  @Getter
  @Setter
  @NotNull
	private Date startDate;

  @Getter
  @Setter
  @NotNull
	private Date endDate;

  @Getter
  @Setter
  @Nullable
  private Long tempId;
}
