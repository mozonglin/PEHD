package com.example.pehd.repository;

import com.example.pehd.entity.MorningExerciseCheckinCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MorningExerciseCheckinCodeRepository extends JpaRepository<MorningExerciseCheckinCode, String> {
    
    /**
     * 根据唯一码查找签到码
     */
    Optional<MorningExerciseCheckinCode> findByUniqueCode(String uniqueCode);
    
    /**
     * 根据早操ID和学号查找签到码
     */
    Optional<MorningExerciseCheckinCode> findByExerciseIdAndStudentId(String exerciseId, String studentId);
    
    /**
     * 查找指定早操的所有签到码
     */
    List<MorningExerciseCheckinCode> findByExerciseIdOrderByCreatedAtDesc(String exerciseId);
    
    /**
     * 查找指定学生的签到码
     */
    List<MorningExerciseCheckinCode> findByStudentIdOrderByCreatedAtDesc(String studentId);
    
    /**
     * 查找有效且未使用的签到码
     */
    @Query("SELECT mecc FROM MorningExerciseCheckinCode mecc WHERE mecc.uniqueCode = :uniqueCode AND mecc.isValid = true AND mecc.isUsed = false")
    Optional<MorningExerciseCheckinCode> findValidUnusedCode(@Param("uniqueCode") String uniqueCode);
    
    /**
     * 查找过期的签到码
     */
    @Query("SELECT mecc FROM MorningExerciseCheckinCode mecc WHERE mecc.expiresAt < :currentTimestamp")
    List<MorningExerciseCheckinCode> findExpiredCodes(@Param("currentTimestamp") Long currentTimestamp);
    
    /**
     * 删除过期的签到码
     */
    @Modifying
    @Query("DELETE FROM MorningExerciseCheckinCode mecc WHERE mecc.expiresAt < :currentTimestamp")
    void deleteExpiredCodes(@Param("currentTimestamp") Long currentTimestamp);
    
    /**
     * 检查签到码是否存在
     */
    boolean existsByUniqueCode(String uniqueCode);
    
    /**
     * 检查学生是否已有有效的签到码
     */
    @Query("SELECT COUNT(mecc) > 0 FROM MorningExerciseCheckinCode mecc WHERE mecc.exerciseId = :exerciseId AND mecc.studentId = :studentId AND mecc.isValid = true AND mecc.isUsed = false AND mecc.expiresAt > :currentTimestamp")
    boolean hasValidCheckinCode(@Param("exerciseId") String exerciseId, 
                              @Param("studentId") String studentId, 
                              @Param("currentTimestamp") Long currentTimestamp);
    
    /**
     * 标记签到码为已使用
     */
    @Modifying
    @Query("UPDATE MorningExerciseCheckinCode mecc SET mecc.isUsed = true, mecc.usedAt = :usedAt, mecc.usedBy = :usedBy WHERE mecc.uniqueCode = :uniqueCode")
    void markAsUsed(@Param("uniqueCode") String uniqueCode, 
                   @Param("usedAt") LocalDateTime usedAt, 
                   @Param("usedBy") String usedBy);
    
    /**
     * 使签到码失效
     */
    @Modifying
    @Query("UPDATE MorningExerciseCheckinCode mecc SET mecc.isValid = false WHERE mecc.exerciseId = :exerciseId AND mecc.studentId = :studentId")
    void invalidateCodesForStudent(@Param("exerciseId") String exerciseId, 
                                 @Param("studentId") String studentId);
}


