package com.example.pehd.repository;

import com.example.pehd.entity.MorningExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MorningExerciseRepository extends JpaRepository<MorningExercise, String> {
    
    /**
     * 查找当前活跃的早操考勤活动
     */
    @Query("SELECT me FROM MorningExercise me WHERE me.isActive = true AND me.startTime <= :currentTime AND me.endTime >= :currentTime ORDER BY me.createdAt DESC")
    Optional<MorningExercise> findCurrentActiveExercise(@Param("currentTime") LocalDateTime currentTime);
    
    /**
     * 根据日期查找早操考勤活动
     */
    List<MorningExercise> findByDateOrderByCreatedAtDesc(LocalDate date);
    
    /**
     * 查找指定日期范围内的早操考勤活动
     */
    @Query("SELECT me FROM MorningExercise me WHERE me.date BETWEEN :startDate AND :endDate ORDER BY me.date DESC, me.createdAt DESC")
    List<MorningExercise> findByDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * 查找活跃状态的早操考勤活动
     */
    List<MorningExercise> findByIsActiveOrderByCreatedAtDesc(Boolean isActive);
    
    /**
     * 根据创建者查找早操考勤活动
     */
    List<MorningExercise> findByCreatedByOrderByCreatedAtDesc(String createdBy);
    
    /**
     * 查找今天的早操考勤活动
     */
    @Query("SELECT me FROM MorningExercise me WHERE me.date = :today ORDER BY me.createdAt DESC")
    List<MorningExercise> findTodayExercises(@Param("today") LocalDate today);
    
    /**
     * 检查指定时间是否存在冲突的早操考勤活动
     */
    @Query("SELECT COUNT(me) > 0 FROM MorningExercise me WHERE me.date = :date AND me.isActive = true AND " +
           "((me.startTime <= :startTime AND me.endTime > :startTime) OR " +
           "(me.startTime < :endTime AND me.endTime >= :endTime) OR " +
           "(me.startTime >= :startTime AND me.endTime <= :endTime))")
    boolean existsConflictingExercise(@Param("date") LocalDate date, 
                                    @Param("startTime") LocalDateTime startTime, 
                                    @Param("endTime") LocalDateTime endTime);
    
    /**
     * 更新签到人数
     */
    @Query("UPDATE MorningExercise me SET me.checkedInCount = me.checkedInCount + 1 WHERE me.id = :exerciseId")
    void incrementCheckedInCount(@Param("exerciseId") String exerciseId);
    
    /**
     * 更新签退人数
     */
    @Query("UPDATE MorningExercise me SET me.checkedOutCount = me.checkedOutCount + 1 WHERE me.id = :exerciseId")
    void incrementCheckedOutCount(@Param("exerciseId") String exerciseId);
    
    /**
     * 根据具体日期查找早操活动（单个结果）
     */
    Optional<MorningExercise> findByDate(LocalDate date);
}


