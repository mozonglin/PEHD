package com.example.pehd.repository;

import com.example.pehd.entity.ExerciseType;
import com.example.pehd.entity.HomeworkScore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HomeworkScoreRepository extends JpaRepository<HomeworkScore, String> {
    
    /**
     * 查询某个学生的作业成绩列表（带分页）
     */
    Page<HomeworkScore> findByStudentIdOrderByTimestampDesc(String studentId, Pageable pageable);
    
    /**
     * 查询某个学生指定项目的作业成绩列表（带分页）
     */
    Page<HomeworkScore> findByStudentIdAndExerciseTypeOrderByTimestampDesc(
        String studentId, ExerciseType exerciseType, Pageable pageable);
    
    /**
     * 查询某个学生的所有作业记录
     */
    List<HomeworkScore> findByStudentIdOrderByTimestampDesc(String studentId);
    
    /**
     * 查询某个学生指定项目的所有记录
     */
    List<HomeworkScore> findByStudentIdAndExerciseTypeOrderByTimestampDesc(
        String studentId, ExerciseType exerciseType);
    
    /**
     * 查询某个学生指定项目的最佳成绩
     */
    @Query("SELECT h FROM HomeworkScore h WHERE h.studentId = :studentId " +
           "AND h.exerciseType = :exerciseType " +
           "ORDER BY h.count DESC, h.timestamp DESC")
    Optional<HomeworkScore> findBestScore(@Param("studentId") String studentId, 
                                           @Param("exerciseType") ExerciseType exerciseType);
    
    /**
     * 查询某个学生某个项目的提交次数
     */
    long countByStudentIdAndExerciseType(String studentId, ExerciseType exerciseType);
    
    /**
     * 查询某个学生的总提交次数
     */
    long countByStudentId(String studentId);
    
    /**
     * 查询班级所有学生在指定日期的作业记录（使用范围查询以利用timestamp索引）
     */
    @Query("SELECT h FROM HomeworkScore h " +
           "JOIN User u ON h.studentId = u.studentId " +
           "WHERE u.className = :className " +
           "AND h.timestamp >= :startTime " +
           "AND h.timestamp < :endTime " +
           "ORDER BY h.timestamp DESC")
    List<HomeworkScore> findByClassNameAndDateRange(@Param("className") String className, 
                                                      @Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime);
    
    /**
     * 查询班级所有学生在指定时间范围内的作业记录（使用范围查询以利用timestamp索引）
     */
    @Query("SELECT h FROM HomeworkScore h " +
           "JOIN User u ON h.studentId = u.studentId " +
           "WHERE u.className = :className " +
           "AND h.timestamp >= :startTime " +
           "ORDER BY h.timestamp DESC")
    List<HomeworkScore> findByClassNameAfterTime(@Param("className") String className,
                                                   @Param("startTime") LocalDateTime startTime);
}

