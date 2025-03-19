package manthonytat.resourcing.job;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
import manthonytat.resourcing.user.User;
import manthonytat.resourcing.user.UserService;

@RestController
@RequestMapping("/jobs")
public class JobController {
  @Autowired
  private JobService jobService;

  @Autowired
  private UserService userService;

  @GetMapping
  public ResponseEntity<List<JobDTO>> getJobs(@RequestParam(value = "assigned", required = false) Boolean assigned) {
    User user = this.userService.getAuthenticatedUser();
    if (assigned == null && this.userService.getAuthenticatedRole("ADMIN")) {
      List<JobDTO> allJobs = this.jobService.findAll();
      return new ResponseEntity<List<JobDTO>>(allJobs, HttpStatus.OK);
    } else if (Boolean.TRUE.equals(assigned)) {
      List<JobDTO> assignedJobs = this.jobService.findAssignedById(user.getId());
      return new ResponseEntity<List<JobDTO>>(assignedJobs, HttpStatus.OK);
    } else {
      List<JobDTO> unassignedJobs = this.jobService.findUnassignedJobs();
      return new ResponseEntity<List<JobDTO>>(unassignedJobs, HttpStatus.OK);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<JobDTO> getById(@PathVariable Long id) {
    User user = this.userService.getAuthenticatedUser();
    if (this.userService.getAuthenticatedRole("ADMIN")) {
      Optional<JobDTO> foundJob = this.jobService.findById(id);
      if (foundJob.isPresent()) {
        return new ResponseEntity<JobDTO>(foundJob.get(), HttpStatus.OK);
      }
      throw new NotFoundException(String.format("Job with id: %d does not exist, could not find job.", id));
    } else {
      Optional<JobDTO> foundJob = jobService.findByIdIfAssignedToTemp(id, user.getId());
      if (foundJob.isPresent()) {
        return new ResponseEntity<JobDTO>(foundJob.get(), HttpStatus.OK);
      }
      throw new NotFoundException(String.format("Job with id: %d does not exist, could not find job.", id));
    }
  }

  @PostMapping
  public ResponseEntity<Job> createJob(@Valid @RequestBody JobCreateDTO data) {
    if (!this.userService.getAuthenticatedRole("ADMIN")) {
      throw new AccessDeniedException("You do not have permission to create job.");
    }
    Job newJob = this.jobService.create(data);
    return new ResponseEntity<Job>(newJob, HttpStatus.CREATED);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Job> updateById(@PathVariable Long id, @Valid @RequestBody JobUpdateDTO data) {
    if (!this.userService.getAuthenticatedRole("ADMIN")) {
      throw new AccessDeniedException("You do not have permission to edit job.");
    }
    Optional<Job> updated = this.jobService.updateById(id, data);
    if (updated.isPresent()) {
      return new ResponseEntity<Job>(updated.get(), HttpStatus.OK);
    }
    throw new NotFoundException(String.format("Job with id: %d does not exist, could not update.", id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Job> deleteById(@PathVariable Long id) {
    if (!this.userService.getAuthenticatedRole("ADMIN")) {
      throw new AccessDeniedException("You do not have permission to delete job.");
    }
    if (this.jobService.deleteById(id)) {
      return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
    throw new NotFoundException(String.format("Job with id: %d does not exist, could not delete.", id));
  }
}
