package com.example.pehd.repository.checkuser;

import com.example.pehd.entity.checkuser.CheckStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CheckStudent数据访问层 - 连接checkuser数据库
 */
@Repository
public interface CheckStudentRepository extends JpaRepository<CheckStudent, String> {
    
    @Query("SELECT cs FROM CheckStudent cs WHERE cs.studentId = :studentId AND cs.name = :name")
    Optional<CheckStudent> findByStudentIdAndName(@Param("studentId") String studentId, @Param("name") String name);
    
    @Query("SELECT cs FROM CheckStudent cs WHERE cs.studentId = :studentId AND cs.name = :name AND cs.school = :school")
    Optional<CheckStudent> findByStudentIdAndNameAndSchool(
        @Param("studentId") String studentId, 
        @Param("name") String name, 
        @Param("school") String school
    );
    
    Optional<CheckStudent> findByStudentId(String studentId);
    
    boolean existsByStudentIdAndName(String studentId, String name);
    
    @Query("SELECT DISTINCT cs.school FROM CheckStudent cs WHERE cs.school IS NOT NULL AND cs.school <> '' ORDER BY cs.school")
    List<String> findDistinctSchools();
}
