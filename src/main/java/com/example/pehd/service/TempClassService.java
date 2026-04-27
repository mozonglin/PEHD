package com.example.pehd.service;

import com.example.pehd.entity.TempClass;
import com.example.pehd.entity.TempClassEnrollment;
import com.example.pehd.repository.TempClassRepository;
import com.example.pehd.repository.TempClassEnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TempClassService {

    @Autowired
    private TempClassRepository tempClassRepository;

    @Autowired
    private TempClassEnrollmentRepository tempClassEnrollmentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<TempClass> getAvailableClasses(String school, String semester) {
        return tempClassRepository.findBySchoolAndSemester(school, semester);
    }

    @Transactional
    public TempClassEnrollment enrollStudent(String studentId, String tempClassId, String school, String semester) {
        if (!isWindowOpen(school, semester)) {
            throw new RuntimeException("选课窗口未开放");
        }

        if (tempClassEnrollmentRepository.existsByStudentIdAndSemester(studentId, semester)) {
            throw new RuntimeException("本学期已选课，不可重复选课");
        }

        TempClass tempClass = tempClassRepository.findById(tempClassId)
                .orElseThrow(() -> new RuntimeException("课程不存在"));

        if (tempClass.getCurrentCount() >= tempClass.getCapacity()) {
            throw new RuntimeException("该课程已满员");
        }

        TempClassEnrollment enrollment = new TempClassEnrollment();
        enrollment.setId(UUID.randomUUID().toString());
        enrollment.setStudentId(studentId);
        enrollment.setTempClassId(tempClassId);
        enrollment.setSchool(school);
        enrollment.setSemester(semester);
        enrollment.setEnrolledAt(LocalDateTime.now());

        tempClassEnrollmentRepository.save(enrollment);

        tempClass.setCurrentCount(tempClass.getCurrentCount() + 1);
        tempClassRepository.save(tempClass);

        return enrollment;
    }

    public Map<String, Object> getMyEnrollment(String studentId, String semester) {
        Optional<TempClassEnrollment> enrollmentOpt = tempClassEnrollmentRepository.findByStudentIdAndSemester(studentId, semester);
        if (enrollmentOpt.isEmpty()) {
            return null;
        }

        TempClassEnrollment enrollment = enrollmentOpt.get();
        Optional<TempClass> tempClassOpt = tempClassRepository.findById(enrollment.getTempClassId());

        Map<String, Object> result = new HashMap<>();
        result.put("enrollment", enrollment);
        tempClassOpt.ifPresent(tc -> result.put("tempClass", tc));
        return result;
    }

    @Transactional
    public void cancelEnrollment(String studentId, String semester) {
        TempClassEnrollment enrollment = tempClassEnrollmentRepository.findByStudentIdAndSemester(studentId, semester)
                .orElseThrow(() -> new RuntimeException("未找到选课记录"));

        String school = enrollment.getSchool();
        if (!isWindowOpen(school, semester)) {
            throw new RuntimeException("选课窗口已关闭，无法取消选课");
        }

        TempClass tempClass = tempClassRepository.findById(enrollment.getTempClassId())
                .orElseThrow(() -> new RuntimeException("课程不存在"));

        tempClassEnrollmentRepository.delete(enrollment);

        tempClass.setCurrentCount(Math.max(0, tempClass.getCurrentCount() - 1));
        tempClassRepository.save(tempClass);
    }

    private boolean isWindowOpen(String school, String semester) {
        LocalDateTime now = LocalDateTime.now();
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM class_selection_windows WHERE school = ? AND semester = ? AND is_active = 1 AND open_time <= ? AND close_time >= ?",
                Integer.class, school, semester, now, now);
        return count != null && count > 0;
    }
}
