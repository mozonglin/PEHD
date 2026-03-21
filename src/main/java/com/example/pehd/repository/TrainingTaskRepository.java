package com.example.pehd.repository;

import com.example.pehd.entity.TrainingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTaskRepository extends JpaRepository<TrainingTask, Long> {
}
