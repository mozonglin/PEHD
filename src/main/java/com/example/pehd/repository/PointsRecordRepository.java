package com.example.pehd.repository;

import com.example.pehd.entity.PointsRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointsRecordRepository extends JpaRepository<PointsRecord, String> {
    
    /**
     * 根据用户ID查找积分记录
     */
    Page<PointsRecord> findByUserIdOrderByEarnedAtDesc(String userId, Pageable pageable);
    
    /**
     * 根据用户ID和活动类型查找积分记录
     */
    Page<PointsRecord> findByUserIdAndActivityTypeOrderByEarnedAtDesc(String userId, String activityType, Pageable pageable);
    
    /**
     * 根据活动ID查找积分记录
     */
    List<PointsRecord> findByActivityId(String activityId);
    
    /**
     * 统计用户总积分
     */
    @Query("SELECT SUM(p.pointsEarned) FROM PointsRecord p WHERE p.userId = :userId")
    Integer sumPointsByUserId(@Param("userId") String userId);
    
    /**
     * 根据活动类型统计用户积分
     */
    @Query("SELECT SUM(p.pointsEarned) FROM PointsRecord p WHERE p.userId = :userId AND p.activityType = :activityType")
    Integer sumPointsByUserIdAndActivityType(@Param("userId") String userId, @Param("activityType") String activityType);
    
    /**
     * 检查是否存在指定用户和活动的积分记录
     */
    boolean existsByUserIdAndActivityId(String userId, String activityId);
}