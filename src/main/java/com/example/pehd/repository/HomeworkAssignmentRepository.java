package com.example.pehd.repository;

import com.example.pehd.entity.HomeworkAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeworkAssignmentRepository extends JpaRepository<HomeworkAssignment, String> {

    List<HomeworkAssignment> findByTempClassIdAndStatus(String tempClassId, String status);
}
