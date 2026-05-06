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
     * 验证学生信息（需要学号+姓名+学校三项全部匹配）
     */
    public StudentValidationResult validateStudent(String studentId, String name, String school) {
        // 先查学号是否存在
        Optional<CheckStudent> byIdOpt = checkStudentRepository.findByStudentId(studentId);
        if (byIdOpt.isEmpty()) {
            return new StudentValidationResult(false, null, null, null, null,
                "学号不存在，请确认学号是否正确");
        }
        
        CheckStudent record = byIdOpt.get();
        
        // 学校不匹配
        if (!record.getSchool().equals(school)) {
            return new StudentValidationResult(false, null, null, null, null,
                "学校与学号不匹配，请确认所选学校是否正确");
        }
        
        // 姓名不匹配
        if (!record.getName().equals(name)) {
            return new StudentValidationResult(false, null, null, null, null,
                "姓名与学号不匹配，请确认姓名是否正确");
        }
        
        return new StudentValidationResult(
            true,
            record.getSchool(),
            record.getCollege(),
            record.getClassName(),
            record.getGender(),
            "验证成功"
        );
    }
    
    /**
     * 获取预导入表中所有学校列表
     */
    public java.util.List<String> getAllSchools() {
        return checkStudentRepository.findDistinctSchools();
    }
    
    public static class StudentValidationResult {
        private final boolean valid;
        private final String school;
        private final String college;
        private final String className;
        private final String gender;
        private final String message;
        
        public StudentValidationResult(boolean valid, String school, String college, String className, String gender, String message) {
            this.valid = valid;
            this.school = school;
            this.college = college;
            this.className = className;
            this.gender = gender;
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

        public String getGender() {
            return gender;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
