package manthonytat.resourcing.temp;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TempRepository extends JpaRepository<Temp, Long> {
  Optional<Temp> findById(Long id);

  List<Temp> findByJobsId(Long jobId);

  @Query("SELECT t FROM Temp t WHERE NOT EXISTS (" +
      "SELECT j FROM Job j WHERE j.temp = t AND " +
      "(j.startDate <= :endDate AND j.endDate >= :startDate))")
  List<Temp> findAvailableTempsForDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

  void deleteByFakeTrue();
}
