package com.example.pehd.repository;

import com.example.pehd.entity.SchoolSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolSettingsRepository extends JpaRepository<SchoolSettings, String> {
    Optional<SchoolSettings> findBySchool(String school);
}
