package manthonytat.resourcing.temp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import manthonytat.resourcing.job.Job;

@Entity
@Table(name = "Temps")
public class Temp {
	
	@Getter
	@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
	
	@Column
	@Getter
	@Setter
  private String firstName;
	
	@Column
	@Getter
	@Setter
  private String lastName;

	@Getter
	@Setter
	@Nullable
	@JsonManagedReference
	@OneToMany(mappedBy = "temp", cascade = CascadeType.ALL)
  private List<Job> jobs;

	public Temp() {}

	public Temp(String firstName, String lastName,List<Job> jobs){
		this.firstName = firstName;
		this.lastName = lastName;
		this.jobs = jobs;
	}
}
