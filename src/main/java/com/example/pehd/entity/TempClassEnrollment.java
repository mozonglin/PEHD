package com.example.pehd.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "temp_class_enrollments",
       uniqueConstraints = @UniqueConstraint(name = "uk_student_semester", columnNames = {"student_id", "semester"}))
public class TempClassEnrollment {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "student_id", length = 36, nullable = false)
    private String studentId;

    @Column(name = "temp_class_id", length = 36, nullable = false)
    private String tempClassId;

    @Column(name = "school", length = 100)
    private String school;

    @Column(name = "semester", length = 50, nullable = false)
    private String semester;

    @Column(name = "enrolled_at")
    private LocalDateTime enrolledAt;

    public TempClassEnrollment() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getTempClassId() { return tempClassId; }
    public void setTempClassId(String tempClassId) { this.tempClassId = tempClassId; }

    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }
}
