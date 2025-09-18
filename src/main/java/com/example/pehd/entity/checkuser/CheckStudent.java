package com.example.pehd.entity.checkuser;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * checkstudent表实体类 - 用于验证学生信息
 * 该实体类连接到checkuser数据库
 */
@Entity
@Table(name = "checkstudent")
public class CheckStudent {
    
    @Id
    @NotBlank(message = "学号不能为空")
    @Size(max = 20, message = "学号长度不能超过20个字符")
    @Column(name = "studentid", nullable = false, length = 20)
    private String studentId;
    
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    @Size(max = 100, message = "学校名称长度不能超过100个字符")
    @Column(name = "school", length = 100)
    private String school;
    
    @Size(max = 100, message = "学院名称长度不能超过100个字符")
    @Column(name = "college", length = 100)
    private String college;
    
    @Size(max = 100, message = "班级名称长度不能超过100个字符")
    @Column(name = "class_name", length = 100)
    private String className;
    
    // Constructors
    public CheckStudent() {}
    
    public CheckStudent(String studentId, String name, String school, String college, String className) {
        this.studentId = studentId;
        this.name = name;
        this.school = school;
        this.college = college;
        this.className = className;
    }
    
    // Getters and Setters
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSchool() {
        return school;
    }
    
    public void setSchool(String school) {
        this.school = school;
    }
    
    public String getCollege() {
        return college;
    }
    
    public void setCollege(String college) {
        this.college = college;
    }
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    @Override
    public String toString() {
        return "CheckStudent{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", school='" + school + '\'' +
                ", college='" + college + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
