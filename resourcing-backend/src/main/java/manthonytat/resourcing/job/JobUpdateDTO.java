package manthonytat.resourcing.job;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class JobUpdateDTO {
  @Getter
  @Setter
  private String name;

  @Getter
	@Setter
	private Date startDate;

  @Getter
	@Setter
	private Date endDate;

  @Getter
  @Setter
  private Long tempId;
}
