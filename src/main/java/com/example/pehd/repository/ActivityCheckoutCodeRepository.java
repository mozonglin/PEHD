package com.example.pehd.repository;

import com.example.pehd.entity.ActivityCheckoutCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityCheckoutCodeRepository extends JpaRepository<ActivityCheckoutCode, String> {
    
    /**
     * 根据唯一码查找签退码
     */
    Optional<ActivityCheckoutCode> findByUniqueCode(String uniqueCode);
    
    /**
     * 根据活动ID和学生ID查找有效的签退码
     */
    @Query("SELECT a FROM ActivityCheckoutCode a WHERE a.activityId = :activityId AND a.studentId = :studentId AND a.isValid = true AND a.isUsed = false")
    Optional<ActivityCheckoutCode> findValidByActivityIdAndStudentId(@Param("activityId") String activityId, @Param("studentId") String studentId);
    
    /**
     * 使指定活动和学生的所有签退码失效
     */
    @Modifying
    @Query("UPDATE ActivityCheckoutCode a SET a.isValid = false WHERE a.activityId = :activityId AND a.studentId = :studentId")
    void invalidateByActivityIdAndStudentId(@Param("activityId") String activityId, @Param("studentId") String studentId);
    
    /**
     * 删除过期的签退码
     */
    @Modifying
    @Query("DELETE FROM ActivityCheckoutCode a WHERE a.expiresAt < :currentTime")
    int deleteByExpiresAtBefore(@Param("currentTime") Long currentTime);
    
    /**
     * 检查是否存在有效未使用的签退码
     */
    boolean existsByUniqueCodeAndIsValidTrueAndIsUsedFalse(String uniqueCode);
}