package com.example.pehd.repository;

import com.example.pehd.entity.MorningExerciseAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MorningExerciseAttendanceRepository extends JpaRepository<MorningExerciseAttendance, String> {
    
    /**
     * 根据早操ID和学号查找考勤记录
     */
    Optional<MorningExerciseAttendance> findByExerciseIdAndStudentId(String exerciseId, String studentId);
    
    /**
     * 检查学生是否已经签到
     */
    boolean existsByExerciseIdAndStudentId(String exerciseId, String studentId);
    
    /**
     * 根据早操ID查找所有考勤记录
     */
    List<MorningExerciseAttendance> findByExerciseIdOrderByCheckInTimeDesc(String exerciseId);
    
    /**
     * 根据学号查找考勤记录
     */
    List<MorningExerciseAttendance> findByStudentIdOrderByCheckInTimeDesc(String studentId);
    
    /**
     * 查找已签到但未签退的记录
     */
    @Query("SELECT mea FROM MorningExerciseAttendance mea WHERE mea.exerciseId = :exerciseId AND mea.isCheckedOut = false")
    List<MorningExerciseAttendance> findCheckedInButNotCheckedOut(@Param("exerciseId") String exerciseId);
    
    /**
     * 查找已完成签到签退的记录
     */
    @Query("SELECT mea FROM MorningExerciseAttendance mea WHERE mea.exerciseId = :exerciseId AND mea.isCheckedOut = true")
    List<MorningExerciseAttendance> findCompletedAttendance(@Param("exerciseId") String exerciseId);
    
    /**
     * 统计早操的签到人数
     */
    @Query("SELECT COUNT(mea) FROM MorningExerciseAttendance mea WHERE mea.exerciseId = :exerciseId")
    Long countByExerciseId(@Param("exerciseId") String exerciseId);
    
    /**
     * 统计早操的签退人数
     */
    @Query("SELECT COUNT(mea) FROM MorningExerciseAttendance mea WHERE mea.exerciseId = :exerciseId AND mea.isCheckedOut = true")
    Long countCheckedOutByExerciseId(@Param("exerciseId") String exerciseId);
    
    /**
     * 根据签到员查找考勤记录
     */
    List<MorningExerciseAttendance> findByCheckedByOrderByCheckInTimeDesc(String checkedBy);
    
    /**
     * 根据签退操作者查找考勤记录
     */
    List<MorningExerciseAttendance> findByCheckedOutByOrderByCheckOutTimeDesc(String checkedOutBy);
    
    /**
     * 查找特定学生在特定早操中的考勤状态
     */
    @Query("SELECT mea FROM MorningExerciseAttendance mea WHERE mea.exerciseId = :exerciseId AND mea.studentId = :studentId")
    Optional<MorningExerciseAttendance> findAttendanceStatus(@Param("exerciseId") String exerciseId, 
                                                            @Param("studentId") String studentId);
    
    /**
     * 根据早操ID和班级查询考勤记录（关联用户表获取班级信息）
     */
    @Query("SELECT mea.studentId, mea.studentName, u.className, mea.checkInTime, mea.checkInLocation, " +
           "mea.checkOutTime, mea.checkOutLocation, mea.isCheckedOut, mea.pointsEarned, " +
           "mea.checkedByName, mea.checkedOutByName " +
           "FROM MorningExerciseAttendance mea " +
           "LEFT JOIN User u ON mea.studentId = u.studentId " +
           "WHERE mea.exerciseId = :exerciseId AND u.className = :className " +
           "ORDER BY mea.checkInTime DESC")
    List<Object[]> findAttendanceRecordsByDateAndClass(@Param("exerciseId") String exerciseId,
                                                       @Param("className") String className);
}


