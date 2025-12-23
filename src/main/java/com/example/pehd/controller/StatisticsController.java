package com.example.pehd.controller;

import com.example.pehd.dto.*;
import com.example.pehd.entity.User;
import com.example.pehd.repository.UserRepository;
import com.example.pehd.service.StatisticsService;
import com.example.pehd.service.HomeworkScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 统计控制器
 * 提供班级考勤统计、达标率分析、积分排名等功能
 */
@RestController
@RequestMapping("/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {
    
    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);
    
    @Autowired
    private StatisticsService statisticsService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private HomeworkScoreService homeworkScoreService;
    
    /**
     * 1. 查看指定日期的班级早操考勤记录（包含缺勤学生）
     * GET /statistics/class-attendance?date=2025-09-18
     * 注意：签到员只能查看自己班级的数据，不需要传递className参数
     */
    @GetMapping("/class-attendance")
    public ResponseEntity<ApiResponse<List<ClassAttendanceRecordDto>>> getClassAttendanceRecords(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication authentication) {
        try {
            // 验证用户身份
            String userId = getUserId(authentication);
            User currentUser = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            // 验证用户是否为签到员
            if (!isChecker(currentUser)) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "权限不足，仅签到员可以查看考勤记录", null));
            }
            
            // 签到员只能查看自己班级的数据
            String className = currentUser.getClassName();
            if (className == null || className.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "用户班级信息缺失，无法查询考勤记录", null));
            }
            
            List<ClassAttendanceRecordDto> records = statisticsService.getClassAttendanceRecords(date, className);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "查询成功", records));
            
        } catch (Exception e) {
            logger.error("查询班级早操考勤记录失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "查询失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * 2. 获取本班级学生达标情况
     * GET /statistics/student-compliance
     * 注意：签到员只能查看自己班级的学生达标情况
     */
    @GetMapping("/student-compliance")
    public ResponseEntity<ApiResponse<List<StudentComplianceDto>>> getStudentComplianceStatus(
            Authentication authentication) {
        try {
            // 验证用户身份
            String userId = getUserId(authentication);
            User currentUser = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            // 验证用户是否为签到员
            if (!isChecker(currentUser)) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "权限不足，仅签到员可以查看学生达标情况", null));
            }
            
            // 签到员只能查看自己班级的数据
            String className = currentUser.getClassName();
            if (className == null || className.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "用户班级信息缺失，无法查询学生达标情况", null));
            }
            
            List<StudentComplianceDto> compliance = statisticsService.getClassStudentCompliance(className);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "查询成功", compliance));
            
        } catch (Exception e) {
            logger.error("查询学生达标情况失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "查询失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * 3. 获取班级积分排名
     * GET /statistics/points-ranking?rankType=total
     * 注意：签到员只能查看自己班级的积分排名
     */
    @GetMapping("/points-ranking")
    public ResponseEntity<ApiResponse<List<PointsRankingDto>>> getPointsRanking(
            @RequestParam(defaultValue = "total") String rankType,
            Authentication authentication) {
        try {
            // 验证用户身份
            String userId = getUserId(authentication);
            User currentUser = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            // 验证用户是否为签到员
            if (!isChecker(currentUser)) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "权限不足，仅签到员可以查看积分排名", null));
            }
            
            // 验证排名类型
            if (!isValidRankType(rankType)) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "无效的排名类型，支持: total, activity, morning", null));
            }
            
            // 签到员只能查看自己班级的数据
            String className = currentUser.getClassName();
            if (className == null || className.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "用户班级信息缺失，无法查询积分排名", null));
            }
            
            List<PointsRankingDto> rankings = statisticsService.getClassPointsRanking(className, rankType);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "查询成功", rankings));
            
        } catch (Exception e) {
            logger.error("查询积分排名失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "查询失败: " + e.getMessage(), null));
        }
    }
    
    
    /**
     * 4. 获取班级所有人课后作业次数（签到员权限）
     * GET /statistics/homework-scores?date=2024-12-23
     * 如果传入日期，返回该日期的记录；如果不传日期或传null，返回所有日期的记录
     */
    @GetMapping("/homework-scores")
    public ResponseEntity<ApiResponse<List<ClassHomeworkScoreDto>>> getClassHomeworkScores(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Authentication authentication) {
        try {
            // 验证用户身份
            String userId = getUserId(authentication);
            User currentUser = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
            
            // 获取学号
            String studentId = currentUser.getStudentId();
            
            // 调用服务层获取课后作业成绩（权限验证在服务层进行）
            List<ClassHomeworkScoreDto> scores = homeworkScoreService.getClassHomeworkScores(studentId, date);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "查询成功", scores));
            
        } catch (Exception e) {
            logger.error("查询班级课后作业成绩失败", e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "查询失败: " + e.getMessage(), null));
        }
    }
    
    /**
     * 从认证信息中获取用户ID
     */
    private String getUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("用户未登录");
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getId();
        } else if (principal instanceof String) {
            return (String) principal;
        } else {
            throw new RuntimeException("无效的用户认证信息");
        }
    }
    
    /**
     * 检查用户是否为签到员
     */
    private boolean isChecker(User user) {
        return user.getRole().name().equals("CHECKER") || 
               user.getRole().name().equals("SUB_CHECKER");
    }
    
    /**
     * 验证排名类型是否有效
     */
    private boolean isValidRankType(String rankType) {
        return "total".equalsIgnoreCase(rankType) || 
               "activity".equalsIgnoreCase(rankType) || 
               "morning".equalsIgnoreCase(rankType);
    }
}
