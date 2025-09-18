package com.example.pehd.repository;

import com.example.pehd.entity.CheckerAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckerAuthorizationRepository extends JpaRepository<CheckerAuthorization, String> {
    
    /**
     * 根据学号和早操ID查找授权记录
     */
    Optional<CheckerAuthorization> findByStudentIdAndExerciseId(String studentId, String exerciseId);
    
    /**
     * 查找学生在指定早操中的活跃授权
     */
    @Query("SELECT ca FROM CheckerAuthorization ca WHERE ca.studentId = :studentId AND ca.exerciseId = :exerciseId AND ca.isActive = true")
    Optional<CheckerAuthorization> findActiveAuthorizationByStudentAndExercise(@Param("studentId") String studentId, 
                                                                              @Param("exerciseId") String exerciseId);
    
    /**
     * 查找指定早操的所有授权记录
     */
    List<CheckerAuthorization> findByExerciseIdOrderByAuthorizedAtDesc(String exerciseId);
    
    /**
     * 查找指定学生的所有授权记录
     */
    List<CheckerAuthorization> findByStudentIdOrderByAuthorizedAtDesc(String studentId);
    
    /**
     * 查找活跃的授权记录
     */
    List<CheckerAuthorization> findByIsActiveOrderByAuthorizedAtDesc(Boolean isActive);
    
    /**
     * 根据角色查找授权记录
     */
    List<CheckerAuthorization> findByRoleOrderByAuthorizedAtDesc(String role);
    
    /**
     * 查找指定授权人的授权记录
     */
    List<CheckerAuthorization> findByAuthorizedByOrderByAuthorizedAtDesc(String authorizedBy);
    
    /**
     * 检查学生是否已有授权
     */
    boolean existsByStudentIdAndExerciseIdAndIsActive(String studentId, String exerciseId, Boolean isActive);
    
    /**
     * 查找早操中的签到员
     */
    @Query("SELECT ca FROM CheckerAuthorization ca WHERE ca.exerciseId = :exerciseId AND ca.role = :role AND ca.isActive = true")
    List<CheckerAuthorization> findCheckersForExercise(@Param("exerciseId") String exerciseId, @Param("role") CheckerAuthorization.CheckerRole role);
    
    /**
     * 查找早操中的二级管理员
     */
    @Query("SELECT ca FROM CheckerAuthorization ca WHERE ca.exerciseId = :exerciseId AND ca.role = :role AND ca.isActive = true")
    List<CheckerAuthorization> findSubCheckersForExercise(@Param("exerciseId") String exerciseId, @Param("role") CheckerAuthorization.CheckerRole role);
    
    /**
     * 获取用户在早操中的权限角色
     */
    @Query("SELECT ca.role FROM CheckerAuthorization ca WHERE ca.studentId = :studentId AND ca.exerciseId = :exerciseId AND ca.isActive = true")
    Optional<CheckerAuthorization.CheckerRole> findUserRoleInExercise(@Param("studentId") String studentId, 
                                          @Param("exerciseId") String exerciseId);
}
