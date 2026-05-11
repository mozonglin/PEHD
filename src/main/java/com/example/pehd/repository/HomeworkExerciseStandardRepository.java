package com.example.pehd.repository;

import com.example.pehd.entity.HomeworkExerciseStandard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HomeworkExerciseStandardRepository extends JpaRepository<HomeworkExerciseStandard, String> {
    Optional<HomeworkExerciseStandard> findBySchoolAndExerciseType(String school, String exerciseType);
    List<HomeworkExerciseStandard> findBySchool(String school);
}
