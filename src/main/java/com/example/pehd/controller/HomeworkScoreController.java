package com.example.pehd.controller;

import com.example.pehd.dto.*;
import com.example.pehd.entity.ExerciseType;
import com.example.pehd.entity.User;
import com.example.pehd.service.HomeworkScoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/homework-scores")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HomeworkScoreController {
    
    @Autowired
    private HomeworkScoreService homeworkScoreService;
    
    /**
     * 上传课后作业成绩
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<HomeworkScoreDto>> uploadScore(
            @Valid @RequestBody UploadHomeworkScoreRequest request,
            Authentication authentication) {
        try {
            HomeworkScoreDto score = homeworkScoreService.uploadScore(request);
            return ResponseEntity.ok(ApiResponse.success("上传成功", score));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("上传失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取我的作业成绩列表
     */
    @GetMapping("/my-scores")
    public ResponseEntity<ApiResponse<HomeworkScoreListResponse>> getMyScores(
            @RequestParam(required = false) ExerciseType exerciseType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        try {
            String studentId = getStudentId(authentication);
            HomeworkScoreListResponse response = homeworkScoreService.getMyScores(
                studentId, exerciseType, page, size);
            return ResponseEntity.ok(ApiResponse.success("获取成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取我的作业成绩统计
     */
    @GetMapping("/my-stats")
    public ResponseEntity<ApiResponse<HomeworkScoreStatsDto>> getMyStats(
            Authentication authentication) {
        try {
            String studentId = getStudentId(authentication);
            HomeworkScoreStatsDto stats = homeworkScoreService.getMyStats(studentId);
            return ResponseEntity.ok(ApiResponse.success("获取成功", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取最佳成绩
     */
    @GetMapping("/best")
    public ResponseEntity<ApiResponse<BestScoreDto>> getBestScore(
            @RequestParam ExerciseType exerciseType,
            Authentication authentication) {
        try {
            String studentId = getStudentId(authentication);
            BestScoreDto bestScore = homeworkScoreService.getBestScore(studentId, exerciseType);
            
            if (bestScore == null) {
                return ResponseEntity.ok(ApiResponse.success("暂无成绩记录", null));
            }
            
            return ResponseEntity.ok(ApiResponse.success("获取成功", bestScore));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取失败: " + e.getMessage()));
        }
    }
    
    /**
     * 从认证信息中获取学号
     */
    private String getStudentId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("未登录");
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getStudentId();
        }
        
        throw new RuntimeException("无效的认证信息");
    }
}

