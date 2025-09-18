package com.example.pehd.repository;

import com.example.pehd.entity.MorningExerciseCheckoutCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MorningExerciseCheckoutCodeRepository extends JpaRepository<MorningExerciseCheckoutCode, String> {
    
    /**
     * 根据唯一码查找签退码
     */
    Optional<MorningExerciseCheckoutCode> findByUniqueCode(String uniqueCode);
    
    /**
     * 根据早操ID和学号查找签退码
     */
    Optional<MorningExerciseCheckoutCode> findByExerciseIdAndStudentId(String exerciseId, String studentId);
    
    /**
     * 查找指定早操的所有签退码
     */
    List<MorningExerciseCheckoutCode> findByExerciseIdOrderByCreatedAtDesc(String exerciseId);
    
    /**
     * 查找指定学生的签退码
     */
    List<MorningExerciseCheckoutCode> findByStudentIdOrderByCreatedAtDesc(String studentId);
    
    /**
     * 查找有效且未使用的签退码
     */
    @Query("SELECT mecc FROM MorningExerciseCheckoutCode mecc WHERE mecc.uniqueCode = :uniqueCode AND mecc.isValid = true AND mecc.isUsed = false")
    Optional<MorningExerciseCheckoutCode> findValidUnusedCode(@Param("uniqueCode") String uniqueCode);
    
    /**
     * 查找过期的签退码
     */
    @Query("SELECT mecc FROM MorningExerciseCheckoutCode mecc WHERE mecc.expiresAt < :currentTimestamp")
    List<MorningExerciseCheckoutCode> findExpiredCodes(@Param("currentTimestamp") Long currentTimestamp);
    
    /**
     * 删除过期的签退码
     */
    @Modifying
    @Query("DELETE FROM MorningExerciseCheckoutCode mecc WHERE mecc.expiresAt < :currentTimestamp")
    void deleteExpiredCodes(@Param("currentTimestamp") Long currentTimestamp);
    
    /**
     * 检查签退码是否存在
     */
    boolean existsByUniqueCode(String uniqueCode);
    
    /**
     * 检查学生是否已有有效的签退码
     */
    @Query("SELECT COUNT(mecc) > 0 FROM MorningExerciseCheckoutCode mecc WHERE mecc.exerciseId = :exerciseId AND mecc.studentId = :studentId AND mecc.isValid = true AND mecc.isUsed = false AND mecc.expiresAt > :currentTimestamp")
    boolean hasValidCheckoutCode(@Param("exerciseId") String exerciseId, 
                               @Param("studentId") String studentId, 
                               @Param("currentTimestamp") Long currentTimestamp);
    
    /**
     * 标记签退码为已使用
     */
    @Modifying
    @Query("UPDATE MorningExerciseCheckoutCode mecc SET mecc.isUsed = true, mecc.usedAt = :usedAt, mecc.usedBy = :usedBy WHERE mecc.uniqueCode = :uniqueCode")
    void markAsUsed(@Param("uniqueCode") String uniqueCode, 
                   @Param("usedAt") LocalDateTime usedAt, 
                   @Param("usedBy") String usedBy);
    
    /**
     * 使签退码失效
     */
    @Modifying
    @Query("UPDATE MorningExerciseCheckoutCode mecc SET mecc.isValid = false WHERE mecc.exerciseId = :exerciseId AND mecc.studentId = :studentId")
    void invalidateCodesForStudent(@Param("exerciseId") String exerciseId, 
                                 @Param("studentId") String studentId);
}


