package manthonytat.resourcing.job;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import manthonytat.resourcing.exceptions.NotFoundException;

@RestController
@RequestMapping("/jobs")
public class JobController {
  @Autowired
  private JobService jobService;

  @GetMapping
  public ResponseEntity<List<JobDTO>> getAllJobs(@RequestParam(value = "assigned", required = false) Boolean assigned){
    List<JobDTO> allJobs = this.jobService.findAll(assigned);
    return new ResponseEntity<List<JobDTO>>(allJobs,HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<JobDTO> getById(@PathVariable Long id){
    Optional<JobDTO> foundJob = this.jobService.findById(id);
    if(foundJob.isPresent()){
      return new ResponseEntity<JobDTO>(foundJob.get(),HttpStatus.OK);
    }
    throw new NotFoundException(String.format("Job with id: %d does not exist, could not find job.", id));
  }

  @PostMapping
  public ResponseEntity<Job> createJob(@Valid @RequestBody JobCreateDTO data){
    Job newJob = this.jobService.create(data);
    return new ResponseEntity<Job>(newJob,HttpStatus.CREATED);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Job> updateById(@PathVariable Long id, @Valid @RequestBody JobUpdateDTO data){
    Optional<Job> updated = this.jobService.updateById(id, data);
    if(updated.isPresent()){
      return new ResponseEntity<Job>(updated.get(),HttpStatus.OK);
    }
    throw new NotFoundException(String.format("Job with id: %d does not exist, could not update.", id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Job> deleteById(@PathVariable Long id){
    if(this.jobService.deleteById(id)){
      return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
    }
    throw new NotFoundException(String.format("Job with id: %d does not exist, could not delete.", id));
  }
}
