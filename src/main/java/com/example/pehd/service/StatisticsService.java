package com.example.pehd.service;

import com.example.pehd.dto.*;
import com.example.pehd.entity.*;
import com.example.pehd.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计服务类
 * 提供各种统计数据和达标率计算
 */
@Service
public class StatisticsService {
    
    private static final Logger logger = LoggerFactory.getLogger(StatisticsService.class);
    
    @Autowired
    private MorningExerciseAttendanceRepository attendanceRepository;
    
    @Autowired
    private MorningExerciseRepository morningExerciseRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SchoolAdminRepository schoolAdminRepository;
    
    /**
     * 获取指定日期的班级早操考勤记录（包含缺勤学生）
     */
    public List<ClassAttendanceRecordDto> getClassAttendanceRecords(LocalDate date, String className) {
        logger.info("获取班级早操考勤记录 - 日期: {}, 班级: {}", date, className);
        
        // 获取班级所有学生来确定学院信息
        List<User> classStudents = userRepository.findByClassName(className);
        if (classStudents.isEmpty()) {
            logger.warn("班级中没有学生: {}", className);
            return new ArrayList<>();
        }
        
        // 通过班级学生的学院信息查找对应的早操活动
        String college = classStudents.get(0).getCollege();
        if (college == null || college.trim().isEmpty()) {
            logger.warn("班级学生学院信息缺失: {}", className);
            return new ArrayList<>();
        }
        
        // 查找指定日期和学院的早操活动
        Optional<MorningExercise> exercise = morningExerciseRepository.findByDateAndCollege(date, college);
        if (!exercise.isPresent()) {
            logger.warn("指定日期和学院没有早操活动 - 日期: {}, 学院: {}", date, college);
            return new ArrayList<>();
        }
        
        String exerciseId = exercise.get().getId();
        
        // 获取已签到的学生记录
        List<Object[]> attendanceResults = attendanceRepository.findAttendanceRecordsByDateAndClass(
            exerciseId, className);
        
        // 转换已签到记录为Map，方便查找
        Map<String, ClassAttendanceRecordDto> attendanceMap = attendanceResults.stream()
            .collect(Collectors.toMap(
                row -> (String) row[0], // student_id作为key
                row -> new ClassAttendanceRecordDto(
                    (String) row[0],  // student_id
                    (String) row[1],  // student_name
                    (String) row[2],  // class_name
                    (java.time.LocalDateTime) row[3],  // check_in_time
                    (String) row[4],  // check_in_location
                    (java.time.LocalDateTime) row[5],  // check_out_time
                    (String) row[6],  // check_out_location
                    (Boolean) row[7], // is_checked_out
                    (Integer) row[8], // points_earned
                    (String) row[9],  // checked_by_name
                    (String) row[10]  // checked_out_by_name
                )
            ));
        
        // 生成完整的考勤记录列表（包含缺勤学生）
        List<ClassAttendanceRecordDto> allRecords = new ArrayList<>();
        
        for (User student : classStudents) {
            if (attendanceMap.containsKey(student.getStudentId())) {
                // 已签到的学生
                allRecords.add(attendanceMap.get(student.getStudentId()));
            } else {
                // 缺勤的学生
                ClassAttendanceRecordDto absentRecord = new ClassAttendanceRecordDto();
                absentRecord.setStudentId(student.getStudentId());
                absentRecord.setStudentName(student.getName());
                absentRecord.setClassName(student.getClassName());
                absentRecord.setCheckInTime(null);
                absentRecord.setCheckInLocation(null);
                absentRecord.setCheckOutTime(null);
                absentRecord.setCheckOutLocation(null);
                absentRecord.setIsCheckedOut(false);
                absentRecord.setPointsEarned(0);
                absentRecord.setCheckedByName("缺勤");
                absentRecord.setCheckedOutByName(null);
                allRecords.add(absentRecord);
            }
        }
        
        // 按考勤状态排序：已签到的在前，缺勤的在后
        allRecords.sort((a, b) -> {
            if (a.getCheckInTime() == null && b.getCheckInTime() != null) return 1;
            if (a.getCheckInTime() != null && b.getCheckInTime() == null) return -1;
            if (a.getCheckInTime() != null && b.getCheckInTime() != null) {
                return b.getCheckInTime().compareTo(a.getCheckInTime());
            }
            return a.getStudentName().compareTo(b.getStudentName());
        });
        
        return allRecords;
    }
    
    /**
     * 获取本班级学生达标情况统计（仅供签到员查看自己班级）
     */
    public List<StudentComplianceDto> getClassStudentCompliance(String className) {
        logger.info("获取班级学生达标情况 - 班级: {}", className);
        
        // 查询班级学生信息
        List<User> students = userRepository.findByClassName(className);
        
        // 获取第一个学生的学校信息来查找管理员目标设置
        if (students.isEmpty()) {
            logger.warn("班级中没有学生: {}", className);
            return new ArrayList<>();
        }
        
        String school = students.get(0).getSchool();
        
        // 获取管理员设置的目标
        Optional<SchoolAdmin> adminOpt = schoolAdminRepository.findSchoolAdminBySchool(school);
        Integer weeklyTarget = 0;
        Integer monthlyTarget = 0;
        Integer totalTarget = 0;
        
        if (adminOpt.isPresent()) {
            SchoolAdmin admin = adminOpt.get();
            weeklyTarget = admin.getWeeklyTarget() != null ? admin.getWeeklyTarget() : 0;
            monthlyTarget = admin.getMonthlyTarget() != null ? admin.getMonthlyTarget() : 0;
            totalTarget = admin.getTotalTarget() != null ? admin.getTotalTarget() : 0;
        }
        
        final Integer finalWeeklyTarget = weeklyTarget;
        final Integer finalMonthlyTarget = monthlyTarget;
        final Integer finalTotalTarget = totalTarget;
        
        return students.stream().map(student -> {
            // 计算平均周积分和月积分
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime createdAt = student.getCreatedAt();
            
            // 计算从创建日期到现在的天数、周数、月数
            long daysSinceCreation = ChronoUnit.DAYS.between(createdAt, now);
            double weeksSinceCreation = Math.max(1.0, daysSinceCreation / 7.0);
            double monthsSinceCreation = Math.max(1.0, daysSinceCreation / 30.0);
            
            // 计算平均积分
            double averageWeeklyPoints = student.getPoints().doubleValue() / weeksSinceCreation;
            double averageMonthlyPoints = student.getPoints().doubleValue() / monthsSinceCreation;
            
            return new StudentComplianceDto(
                student.getStudentId(),
                student.getName(),
                student.getClassName(),
                student.getPoints(),
                student.getPeActivityPoints(),
                student.getMorningExercisePoints(),
                finalWeeklyTarget,
                finalMonthlyTarget,
                finalTotalTarget,
                averageWeeklyPoints,
                averageMonthlyPoints
            );
        }).collect(Collectors.toList());
    }
    
    /**
     * 获取班级积分排名（仅限班级内排名）
     */
    public List<PointsRankingDto> getClassPointsRanking(String className, String rankType) {
        logger.info("获取班级积分排名 - 班级: {}, 排名类型: {}", className, rankType);
        
        // 获取班级所有学生
        List<User> students = userRepository.findByClassName(className);
        
        // 根据排名类型排序
        switch (rankType.toLowerCase()) {
            case "total":
                students.sort((a, b) -> b.getPoints().compareTo(a.getPoints()));
                break;
            case "activity":
                students.sort((a, b) -> b.getPeActivityPoints().compareTo(a.getPeActivityPoints()));
                break;
            case "morning":
                students.sort((a, b) -> b.getMorningExercisePoints().compareTo(a.getMorningExercisePoints()));
                break;
            default:
                throw new IllegalArgumentException("无效的排名类型: " + rankType);
        }
        
        List<PointsRankingDto> rankings = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            User user = students.get(i);
            rankings.add(new PointsRankingDto(
                i + 1,  // 排名
                user.getStudentId(),
                user.getName(),
                user.getClassName(),
                user.getPoints(),
                user.getPeActivityPoints(),
                user.getMorningExercisePoints()
            ));
        }
        
        return rankings;
    }
    
}
