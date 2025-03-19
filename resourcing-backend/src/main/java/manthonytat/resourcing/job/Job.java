package manthonytat.resourcing.job;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import manthonytat.resourcing.temp.Temp;

@Entity
@Table(name = "Jobs")
public class Job {

	@Getter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	@Getter
	@Setter
	private String name;

	@Column
	@Getter
	@Setter
	private Date startDate;

	@Column
	@Getter
	@Setter
	private Date endDate;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "temp_id")
	@JsonBackReference
	private Temp temp;

	@Getter
	@Setter
	private boolean fake = false;

	public Job() {
	}

	public Job(String name, Date startDate, Date endDate, Temp temp) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.temp = temp;
	}

	public Job(String name, Date startDate, Date endDate, Temp temp, boolean fake) {
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.temp = temp;
		this.fake = fake;
	}
}
