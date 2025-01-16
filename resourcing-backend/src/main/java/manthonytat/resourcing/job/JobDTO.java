package manthonytat.resourcing.job;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import manthonytat.resourcing.temp.TempDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobDTO {

  @Getter
  @Setter
  private Long id;

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
  private TempDTO temp;

  public JobDTO(Long id, String name, Date startDate, Date endDate,TempDTO temp) {
    this.id = id;
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.temp = temp;
  }

  public JobDTO(Long id, String name, Date startDate, Date endDate) {
    this.id = id;
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}
