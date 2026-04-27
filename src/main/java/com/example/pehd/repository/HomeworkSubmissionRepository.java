package com.example.pehd.repository;

import com.example.pehd.entity.HomeworkSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeworkSubmissionRepository extends JpaRepository<HomeworkSubmission, String> {

    Optional<HomeworkSubmission> findByAssignmentIdAndStudentId(String assignmentId, String studentId);

    List<HomeworkSubmission> findByStudentId(String studentId);
}
