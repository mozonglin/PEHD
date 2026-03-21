package com.example.pehd.repository;

import com.example.pehd.entity.TrainingTaskRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingTaskRecordRepository extends JpaRepository<TrainingTaskRecord, Long> {
    List<TrainingTaskRecord> findByUserId(String userId);
    Optional<TrainingTaskRecord> findByTaskIdAndUserId(Long taskId, String userId);

    @Query("SELECT r FROM TrainingTaskRecord r JOIN r.task t WHERE r.userId = :userId AND t.status = 'active' ORDER BY t.endDate ASC")
    List<TrainingTaskRecord> findActiveRecordsByUserId(@Param("userId") String userId);
}
