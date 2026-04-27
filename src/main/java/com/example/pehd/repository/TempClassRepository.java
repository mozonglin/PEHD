package com.example.pehd.repository;

import com.example.pehd.entity.TempClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TempClassRepository extends JpaRepository<TempClass, String> {

    List<TempClass> findBySchoolAndSemester(String school, String semester);
}
