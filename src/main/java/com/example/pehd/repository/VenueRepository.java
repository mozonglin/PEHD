package com.example.pehd.repository;

import com.example.pehd.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<Venue, String> {

    List<Venue> findByIsDeletedFalseAndStatus(String status);

    List<Venue> findByIsDeletedFalse();

    Optional<Venue> findByIdAndIsDeletedFalse(String id);

    List<Venue> findBySchoolAndIsDeletedFalseAndStatus(String school, String status);

    List<Venue> findBySchoolAndIsDeletedFalse(String school);
}
