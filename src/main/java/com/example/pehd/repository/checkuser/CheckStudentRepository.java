package com.example.pehd.repository.checkuser;

import com.example.pehd.entity.checkuser.CheckStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * CheckStudent数据访问层 - 连接checkuser数据库
 */
@Repository
public interface CheckStudentRepository extends JpaRepository<CheckStudent, String> {
    
    /**
     * 根据学号和姓名查找学生信息
     * @param studentId 学号
     * @param name 姓名
     * @return 匹配的学生信息
     */
    @Query("SELECT cs FROM CheckStudent cs WHERE cs.studentId = :studentId AND cs.name = :name")
    Optional<CheckStudent> findByStudentIdAndName(@Param("studentId") String studentId, @Param("name") String name);
    
    /**
     * 检查学号和姓名是否存在
     * @param studentId 学号
     * @param name 姓名
     * @return 是否存在
     */
    boolean existsByStudentIdAndName(String studentId, String name);
}
