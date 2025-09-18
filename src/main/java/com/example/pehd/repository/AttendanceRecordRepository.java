package com.example.pehd.repository;

import com.example.pehd.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, String> {
    
    /**
     * 根据活动ID查找签到记录
     */
    List<AttendanceRecord> findByActivityId(String activityId);
    
    /**
     * 根据用户ID查找签到记录
     */
    List<AttendanceRecord> findByUserId(String userId);
    
    /**
     * 查找用户在特定活动的签到记录
     */
    Optional<AttendanceRecord> findByActivityIdAndUserId(String activityId, String userId);
    
    /**
     * 检查用户是否已签到特定活动
     */
    boolean existsByActivityIdAndUserId(String activityId, String userId);
    
    /**
     * 根据学号查找签到记录
     */
    List<AttendanceRecord> findByStudentId(String studentId);
    
    /**
     * 根据活动ID和学号查找签到记录
     */
    Optional<AttendanceRecord> findByActivityIdAndStudentId(String activityId, String studentId);
    
    /**
     * 检查学号是否已签到特定活动
     */
    boolean existsByActivityIdAndStudentId(String activityId, String studentId);
    
    /**
     * 统计活动的签到人数
     */
    @Query("SELECT COUNT(a) FROM AttendanceRecord a WHERE a.activityId = :activityId")
    long countByActivityId(@Param("activityId") String activityId);
    
    /**
     * 根据二维码数据查找签到记录
     */
    List<AttendanceRecord> findByQrCodeData(String qrCodeData);
} 