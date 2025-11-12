package com.example.pehd.repository;

import com.example.pehd.entity.SunshineRunRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SunshineRunRepository extends JpaRepository<SunshineRunRecord, String> {
    
    /**
     * 查找用户的阳光跑记录（分页）
     */
    List<SunshineRunRecord> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
    
    /**
     * 查找班级的阳光跑记录（分页）
     */
    List<SunshineRunRecord> findByClassNameOrderByCreatedAtDesc(String className, Pageable pageable);
    
    /**
     * 统计班级排名
     */
    @Query("SELECT new map(r.userName as userName, r.studentId as studentId, " +
           "SUM(r.totalDistance) as totalDistance, COUNT(r) as totalRuns, SUM(r.totalDuration) as totalDuration) " +
           "FROM SunshineRunRecord r WHERE r.className = :className " +
           "GROUP BY r.userName, r.studentId ORDER BY SUM(r.totalDistance) DESC")
    List<Object> findClassRanking(@Param("className") String className);
}

