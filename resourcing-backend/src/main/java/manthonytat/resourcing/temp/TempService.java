package manthonytat.resourcing.temp;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import manthonytat.resourcing.exceptions.NotFoundException;
import manthonytat.resourcing.job.Job;
import manthonytat.resourcing.job.JobDTO;
import manthonytat.resourcing.job.JobRepository;
@Service
@Transactional
public class TempService {
  @Autowired
  private TempRepository tempRepository;

  @Autowired
  private JobRepository jobRepository;


  public List<TempDTO> findAll(){
    return this.tempRepository.findAll().stream()
      .map(this::mapToTempDTO)
      .collect(Collectors.toList());
  }

  public Optional<TempDTO> findById(Long id){
    Optional<Temp> foundTemp = this.tempRepository.findById(id);
    return foundTemp.map(this::mapToTempDTO);
  }

  public List<TempDTO> getAvailableTemps(Long jobId){
    Optional<Job> foundJob = this.jobRepository.findById(jobId);
    if(foundJob.isPresent()){
      Job job = foundJob.get();
      return this.tempRepository.findAvailableTempsForDateRange(job.getStartDate(), job.getEndDate()).stream()
        .map(this::mapToTempDTO)
        .collect(Collectors.toList());
    } 
     throw new NotFoundException(String.format("Job with id: %d does not exist, could not find job.", jobId));
  }

  public Temp create(TempCreateDTO data){
    String firstName = data.getFirstName();
    String lastName = data.getLastName();
    Temp newTemp = new Temp(firstName,lastName,null);
    return this.tempRepository.save(newTemp);
  }

  public Optional<Temp> updateById(Long id, TempUpdateDTO data){
    Optional<Temp> foundTemp = this.tempRepository.findById(id);
    if(foundTemp.isPresent()){
      Temp toUpdate = foundTemp.get();
      if(data.getFirstName() != null){
        toUpdate.setFirstName(data.getFirstName());
      }
      if(data.getLastName() != null){
        toUpdate.setLastName(data.getLastName());
      }
      Temp updatedTemp = this.tempRepository.save(toUpdate);
      return Optional.of(updatedTemp);
    }
    return foundTemp;
  }

  public boolean deleteById(Long id){
    Optional<Temp> foundTemp = this.tempRepository.findById(id);
    if(foundTemp.isPresent()){
      this.tempRepository.delete(foundTemp.get());
      return true;
    }
    return false;
  }

  private TempDTO mapToTempDTO(Temp temp) {
    List<JobDTO> jobs = temp.getJobs().stream()
      .map(job -> new JobDTO(job.getId(), job.getName(), job.getStartDate(), job.getEndDate()))
      .collect(Collectors.toList());
      return new TempDTO(temp.getId(), temp.getFirstName(), temp.getLastName(), jobs);
    }
}