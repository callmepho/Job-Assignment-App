package manthonytat.resourcing.job;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
  List<Job> findByTempIsNull();

  List<Job> findByTempIsNotNull();

  List<Job> findByTempId(Long tempId);

  Optional<Job> findByIdAndTempId(Long jobId, Long tempId);

  void deleteByFakeTrue();
}
