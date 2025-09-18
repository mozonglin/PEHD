package com.example.pehd.service;

import com.example.pehd.entity.checkuser.CheckStudent;
import com.example.pehd.repository.checkuser.CheckStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 学生信息验证服务
 * 用于验证学生信息是否在checkuser数据库中存在
 */
@Service
@Transactional(transactionManager = "checkUserTransactionManager")
public class StudentValidationService {
    
    @Autowired
    private CheckStudentRepository checkStudentRepository;
    
    /**
     * 验证学生信息
     * @param studentId 学号
     * @param name 姓名
     * @return 验证结果，包含学校和学院信息
     */
    public StudentValidationResult validateStudent(String studentId, String name) {
        Optional<CheckStudent> checkStudentOpt = checkStudentRepository.findByStudentIdAndName(studentId, name);
        
        if (checkStudentOpt.isPresent()) {
            CheckStudent checkStudent = checkStudentOpt.get();
            return new StudentValidationResult(
                true, 
                checkStudent.getSchool(), 
                checkStudent.getCollege(),
                checkStudent.getClassName(),
                "验证成功"
            );
        } else {
            return new StudentValidationResult(
                false, 
                null, 
                null,
                null,
                "学号和姓名不匹配，请检查输入信息"
            );
        }
    }
    
    /**
     * 学生验证结果类
     */
    public static class StudentValidationResult {
        private final boolean valid;
        private final String school;
        private final String college;
        private final String className;
        private final String message;
        
        public StudentValidationResult(boolean valid, String school, String college, String className, String message) {
            this.valid = valid;
            this.school = school;
            this.college = college;
            this.className = className;
            this.message = message;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getSchool() {
            return school;
        }
        
        public String getCollege() {
            return college;
        }
        
        public String getClassName() {
            return className;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
