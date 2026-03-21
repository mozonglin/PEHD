package com.example.pehd.repository;

import com.example.pehd.entity.StudentFitnessProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentFitnessProfileRepository extends JpaRepository<StudentFitnessProfile, Long> {
    Optional<StudentFitnessProfile> findByUserId(String userId);
    Optional<StudentFitnessProfile> findByStudentId(String studentId);
}
