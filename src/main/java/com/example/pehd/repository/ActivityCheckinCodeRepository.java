package com.example.pehd.repository;

import com.example.pehd.entity.ActivityCheckinCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityCheckinCodeRepository extends JpaRepository<ActivityCheckinCode, String> {
    
    /**
     * 根据唯一码查找签到码
     */
    Optional<ActivityCheckinCode> findByUniqueCode(String uniqueCode);
    
    /**
     * 根据活动ID查找签到码
     */
    List<ActivityCheckinCode> findByActivityId(String activityId);
    
    /**
     * 根据学号查找签到码
     */
    List<ActivityCheckinCode> findByStudentId(String studentId);
    
    /**
     * 根据活动ID和学号查找签到码
     */
    List<ActivityCheckinCode> findByActivityIdAndStudentId(String activityId, String studentId);
    
    /**
     * 查找有效的签到码
     */
    @Query("SELECT c FROM ActivityCheckinCode c WHERE c.uniqueCode = :uniqueCode AND c.isValid = true AND c.isUsed = false")
    Optional<ActivityCheckinCode> findValidByUniqueCode(@Param("uniqueCode") String uniqueCode);
    
    /**
     * 查找过期的签到码
     */
    @Query("SELECT c FROM ActivityCheckinCode c WHERE c.expiresAt < :currentTime AND c.isValid = true")
    List<ActivityCheckinCode> findExpiredCodes(@Param("currentTime") Long currentTime);
    
    /**
     * 根据活动ID查找有效的签到码
     */
    @Query("SELECT c FROM ActivityCheckinCode c WHERE c.activityId = :activityId AND c.isValid = true AND c.isUsed = false")
    List<ActivityCheckinCode> findValidByActivityId(@Param("activityId") String activityId);
    
    /**
     * 根据学号查找有效的签到码
     */
    @Query("SELECT c FROM ActivityCheckinCode c WHERE c.studentId = :studentId AND c.isValid = true AND c.isUsed = false")
    List<ActivityCheckinCode> findValidByStudentId(@Param("studentId") String studentId);
    
    /**
     * 使指定活动和学生的签到码失效
     */
    @Modifying
    @Query("UPDATE ActivityCheckinCode c SET c.isValid = false WHERE c.activityId = :activityId AND c.studentId = :studentId")
    void invalidateByActivityIdAndStudentId(@Param("activityId") String activityId, @Param("studentId") String studentId);
    
    /**
     * 删除过期的签到码
     */
    @Modifying
    @Query("DELETE FROM ActivityCheckinCode c WHERE c.expiresAt < :currentTime")
    int deleteByExpiresAtBefore(@Param("currentTime") Long currentTime);
    
    /**
     * 检查活动和学生是否有签到码
     */
    boolean existsByActivityIdAndStudentId(String activityId, String studentId);
} 