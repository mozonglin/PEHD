package com.example.pehd.repository;

import com.example.pehd.entity.Activity;
import com.example.pehd.entity.ActivityApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, String> {
    
    /**
     * 根据类别查找活动
     */
    Page<Activity> findByCategory(String category, Pageable pageable);
    
    /**
     * 根据组织者ID查找活动
     */
    Page<Activity> findByOrganizerId(String organizerId, Pageable pageable);
    
    /**
     * 查找指定时间范围内的活动
     */
    @Query("SELECT a FROM Activity a WHERE a.activityStartTime >= :startTime AND a.activityEndTime <= :endTime")
    List<Activity> findActivitiesByTimeRange(@Param("startTime") LocalDateTime startTime, 
                                           @Param("endTime") LocalDateTime endTime);
    
    /**
     * 查找当前可报名的活动
     */
    @Query("SELECT a FROM Activity a WHERE a.registrationStartTime <= :now AND a.registrationEndTime >= :now")
    List<Activity> findAvailableForRegistration(@Param("now") LocalDateTime now);
    
    /**
     * 查找正在进行的活动
     */
    @Query("SELECT a FROM Activity a WHERE a.activityStartTime <= :now AND a.activityEndTime >= :now")
    List<Activity> findOngoingActivities(@Param("now") LocalDateTime now);
    
    /**
     * 根据标题模糊查找活动
     */
    Page<Activity> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    /**
     * 根据类别和标题查找活动
     */
    Page<Activity> findByCategoryAndTitleContainingIgnoreCase(String category, String title, Pageable pageable);
    
    /**
     * 根据类别查找活动（模糊匹配）
     */
    Page<Activity> findByCategoryContainingIgnoreCase(String category, Pageable pageable);
    
    /**
     * 根据ID列表查找活动
     */
    List<Activity> findByIdIn(List<String> ids);
    
    /**
     * 根据组织者ID查找活动（不分页）
     */
    List<Activity> findByOrganizerId(String organizerId);
    
    // 新增：支持审核状态的查询方法
    
    /**
     * 根据审核状态查找活动
     */
    Page<Activity> findByApprovalStatus(ActivityApprovalStatus approvalStatus, Pageable pageable);
    
    /**
     * 根据类别和审核状态查找活动（模糊匹配）
     */
    Page<Activity> findByCategoryContainingIgnoreCaseAndApprovalStatus(String category, ActivityApprovalStatus approvalStatus, Pageable pageable);
    
    /**
     * 根据ID列表和审核状态查找活动
     */
    List<Activity> findByIdInAndApprovalStatus(List<String> ids, ActivityApprovalStatus approvalStatus);
    
    /**
     * 根据组织者ID和审核状态查找活动
     */
    List<Activity> findByOrganizerIdAndApprovalStatus(String organizerId, ActivityApprovalStatus approvalStatus);
} 