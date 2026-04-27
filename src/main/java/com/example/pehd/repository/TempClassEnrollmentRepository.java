package com.example.pehd.repository;

import com.example.pehd.entity.TempClassEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TempClassEnrollmentRepository extends JpaRepository<TempClassEnrollment, String> {

    Optional<TempClassEnrollment> findByStudentIdAndSemester(String studentId, String semester);

    List<TempClassEnrollment> findByTempClassId(String tempClassId);

    boolean existsByStudentIdAndSemester(String studentId, String semester);

    List<TempClassEnrollment> findByStudentId(String studentId);
}
