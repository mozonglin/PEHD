package com.example.pehd.repository;

import com.example.pehd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    /**
     * 根据学号查找用户
     */
    Optional<User> findByStudentId(String studentId);
    
    /**
     * 根据手机号查找用户
     */
    Optional<User> findByPhoneNumber(String phoneNumber);
    
    /**
     * 根据学号和手机号查找用户
     */
    Optional<User> findByStudentIdAndPhoneNumber(String studentId, String phoneNumber);
    
    /**
     * 根据姓名、学号和手机号查找用户
     */
    Optional<User> findByNameAndStudentIdAndPhoneNumber(String name, String studentId, String phoneNumber);
    
    /**
     * 检查学号是否存在
     */
    boolean existsByStudentId(String studentId);
    
    /**
     * 检查手机号是否存在
     */
    boolean existsByPhoneNumber(String phoneNumber);
    
    /**
     * 根据学号或手机号查找用户
     */
    @Query("SELECT u FROM User u WHERE u.studentId = :identifier OR u.phoneNumber = :identifier")
    Optional<User> findByStudentIdOrPhoneNumber(@Param("identifier") String identifier);
    
    /**
     * 根据ID列表查找用户
     */
    List<User> findByIdIn(List<String> ids);
    
    /**
     * 根据学校和班级查找学生
     */
    List<User> findBySchoolAndClassName(String school, String className);
    
    /**
     * 根据班级查找学生
     */
    List<User> findByClassName(String className);
    
    /**
     * 根据学校查找学生
     */
    List<User> findBySchool(String school);
    
    /**
     * 按总积分排序查找学校的学生
     */
    @Query("SELECT u FROM User u WHERE u.school = :school ORDER BY u.points DESC")
    List<User> findBySchoolOrderByPointsDesc(@Param("school") String school, 
                                           org.springframework.data.domain.Pageable pageable);
    
    /**
     * 按PE活动积分排序查找学校的学生
     */
    @Query("SELECT u FROM User u WHERE u.school = :school ORDER BY u.peActivityPoints DESC")
    List<User> findBySchoolOrderByPeActivityPointsDesc(@Param("school") String school,
                                                      org.springframework.data.domain.Pageable pageable);
    
    /**
     * 按早操积分排序查找学校的学生
     */
    @Query("SELECT u FROM User u WHERE u.school = :school ORDER BY u.morningExercisePoints DESC")
    List<User> findBySchoolOrderByMorningExercisePointsDesc(@Param("school") String school,
                                                           org.springframework.data.domain.Pageable pageable);
    
    /**
     * 获取班级积分统计（按班级分组，统计达标情况）
     */
    @Query("SELECT u.className, " +
           "COUNT(u), " +
           "SUM(CASE WHEN u.points >= (SELECT sa.weeklyTarget FROM SchoolAdmin sa WHERE sa.school = :school AND sa.userType = 'school_admin') THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN u.points >= (SELECT sa.monthlyTarget FROM SchoolAdmin sa WHERE sa.school = :school AND sa.userType = 'school_admin') THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN u.points >= (SELECT sa.totalTarget FROM SchoolAdmin sa WHERE sa.school = :school AND sa.userType = 'school_admin') THEN 1 ELSE 0 END) " +
           "FROM User u WHERE u.school = :school " +
           "GROUP BY u.className ORDER BY u.className")
    List<Object[]> findClassPointsStatistics(@Param("school") String school);
} 