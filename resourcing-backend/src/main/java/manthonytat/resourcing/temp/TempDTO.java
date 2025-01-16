package manthonytat.resourcing.temp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import manthonytat.resourcing.job.JobDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TempDTO {
  
  @Getter
  @Setter
  private Long id;

  @Getter
  @Setter
  private String firstName;

  @Getter
  @Setter
  private String lastName;

  @Getter
  @Setter
  private List<JobDTO> jobs;

  public TempDTO(Long id, String firstName, String lastName, List<JobDTO> jobs) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.jobs = jobs;
  }

  public TempDTO(Long id, String firstName, String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
