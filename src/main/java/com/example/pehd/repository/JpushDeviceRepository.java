package com.example.pehd.repository;

import com.example.pehd.entity.JpushDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpushDeviceRepository extends JpaRepository<JpushDevice, String> {

    Optional<JpushDevice> findByStudentIdAndPlatform(String studentId, String platform);
}
