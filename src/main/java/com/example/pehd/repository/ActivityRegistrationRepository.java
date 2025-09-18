package com.example.pehd.repository;

import com.example.pehd.entity.ActivityRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRegistrationRepository extends JpaRepository<ActivityRegistration, String> {
    
    /**
     * 根据用户ID查找报名记录
     */
    List<ActivityRegistration> findByUserId(String userId);
    
    /**
     * 根据活动ID查找报名记录
     */
    List<ActivityRegistration> findByActivityId(String activityId);
    
    /**
     * 查找用户在特定活动的报名记录
     */
    Optional<ActivityRegistration> findByUserIdAndActivityId(String userId, String activityId);
    
    /**
     * 检查用户是否已报名特定活动
     */
    boolean existsByUserIdAndActivityId(String userId, String activityId);
    
    /**
     * 根据活动ID和状态查找报名记录
     */
    List<ActivityRegistration> findByActivityIdAndStatus(String activityId, ActivityRegistration.RegistrationStatus status);
    
    /**
     * 根据用户ID和状态查找报名记录
     */
    List<ActivityRegistration> findByUserIdAndStatus(String userId, ActivityRegistration.RegistrationStatus status);
    
    /**
     * 根据用户ID和状态查找报名记录（字符串状态）
     */
    @Query("SELECT r FROM ActivityRegistration r WHERE r.userId = :userId AND r.status = :status")
    List<ActivityRegistration> findByUserIdAndStatusString(@Param("userId") String userId, @Param("status") String status);
    
    /**
     * 统计活动的报名人数
     */
    @Query("SELECT COUNT(r) FROM ActivityRegistration r WHERE r.activityId = :activityId AND r.status = 'REGISTERED'")
    long countRegisteredByActivityId(@Param("activityId") String activityId);
    
    /**
     * 删除用户对特定活动的报名记录
     */
    void deleteByUserIdAndActivityId(String userId, String activityId);
} 