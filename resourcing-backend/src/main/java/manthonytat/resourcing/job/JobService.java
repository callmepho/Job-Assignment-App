package manthonytat.resourcing.job;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;

import jakarta.transaction.Transactional;
import manthonytat.resourcing.temp.Temp;
import manthonytat.resourcing.temp.TempDTO;
import manthonytat.resourcing.temp.TempRepository;

@Service
@Transactional
public class JobService {

  @Autowired
  private JobRepository jobRepository;

  @Autowired
  private TempRepository tempRepository;

  public List<JobDTO> findAll() {
    return this.jobRepository.findAll().stream()
        .map(this::mapToJobDTO)
        .collect(Collectors.toList());

  }

  public List<JobDTO> findUnassignedJobs() {
    return this.jobRepository.findByTempIsNull().stream()
        .map(this::mapToJobDTO)
        .collect(Collectors.toList());
  }

  public List<JobDTO> findAssignedById(Long tempId) {
    return this.jobRepository.findByTempId(tempId).stream()
        .map(this::mapToJobDTO)
        .collect(Collectors.toList());
  }

  public Optional<JobDTO> findByIdIfAssignedToTemp(Long jobId, Long tempId) {
    return this.jobRepository.findByIdAndTempId(jobId, tempId)
        .map(this::mapToJobDTO);
  }

  public Optional<JobDTO> findById(Long id) {
    Optional<Job> foundJob = this.jobRepository.findById(id);
    return foundJob.map(this::mapToJobDTO);
  }

  public Job create(JobCreateDTO data) {
    String name = data.getName();
    Date startDate = data.getStartDate();
    Date endDate = data.getEndDate();

    if (data.getTempId() != null) {
      Optional<Temp> foundTemp = this.tempRepository.findById(data.getTempId());
      if (foundTemp.isPresent()) {
        Temp temp = foundTemp.get();
        validateTempAvailability(temp, startDate, endDate);
        Job newJob = new Job(name, startDate, endDate, temp);
        return this.jobRepository.save(newJob);
      }
    }
    Job newJob = new Job(name, startDate, endDate, null);
    return this.jobRepository.save(newJob);
  }

  public Optional<Job> updateById(Long id, JobUpdateDTO data) {
    Optional<Job> foundJob = this.jobRepository.findById(id);

    if (foundJob.isPresent()) {
      Job toUpdate = foundJob.get();

      if (data.getName() != null) {
        toUpdate.setName(data.getName());
      }

      if (data.getStartDate() != null) {
        toUpdate.setStartDate(data.getStartDate());
      }

      if (data.getEndDate() != null) {
        toUpdate.setEndDate(data.getEndDate());
      }

      if (data.getTempId() != null) {
        Optional<Temp> foundTemp = this.tempRepository.findById(data.getTempId());
        if (foundTemp.isPresent()) {
          toUpdate.setTemp(foundTemp.get());
        }
      }
      Job updatedJob = this.jobRepository.save(toUpdate);

      return Optional.of(updatedJob);
    }

    return foundJob;
  }

  public boolean deleteById(Long id) {
    Optional<Job> foundJob = this.jobRepository.findById(id);

    if (foundJob.isPresent()) {
      this.jobRepository.delete(foundJob.get());
      return true;
    }
    return false;
  }

  private void validateTempAvailability(Temp temp, Date startDate, Date endDate) {
    temp.getJobs().forEach(existingJob -> {
      if (!(endDate.before(existingJob.getStartDate()) || startDate.after(existingJob.getEndDate()))) {
        throw new IllegalStateException("Temp is not available for the specified date range");
      }
    });
  }

  private JobDTO mapToJobDTO(Job job) {
    if (job.getTemp() != null) {
      Temp temp = job.getTemp();
      TempDTO tempDTO = new TempDTO(temp.getId(), temp.getFirstName(), temp.getLastName());
      return new JobDTO(job.getId(), job.getName(), job.getStartDate(), job.getEndDate(), tempDTO);
    }
    return new JobDTO(job.getId(), job.getName(), job.getStartDate(), job.getEndDate());
  }

  private Faker faker = new Faker();

  public void createFakeJobs(long number) {
    for (int i = 0; i < number; i++) {
      String name = faker.job().title();
      Date startDate = faker.date().future(10, TimeUnit.DAYS);
      Date endDate = faker.date().future(30, TimeUnit.DAYS, startDate);
      Job newJob = new Job(name, startDate, endDate, null, true);
      this.jobRepository.save(newJob);
    }
  }

  public void deleteFakeJobs() {
    this.jobRepository.deleteByFakeTrue();
  }
}
