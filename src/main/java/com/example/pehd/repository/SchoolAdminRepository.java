package com.example.pehd.repository;

import com.example.pehd.entity.SchoolAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 学校管理员Repository
 * 用于查询users表中的管理员目标设置
 */
@Repository
public interface SchoolAdminRepository extends JpaRepository<SchoolAdmin, String> {
    
    /**
     * 根据学校名称查找校级管理员的目标设置
     * @param school 学校名称
     * @return 校级管理员信息
     */
    @Query("SELECT sa FROM SchoolAdmin sa WHERE sa.school = :school AND sa.userType = 'school_admin'")
    Optional<SchoolAdmin> findSchoolAdminBySchool(@Param("school") String school);
    
    /**
     * 根据学校名称和用户类型查找管理员
     * @param school 学校名称
     * @param userType 用户类型
     * @return 管理员信息
     */
    Optional<SchoolAdmin> findBySchoolAndUserType(String school, String userType);
}
