package com.example.pehd.controller;

import com.example.pehd.dto.ApiResponse;
import com.example.pehd.entity.HomeworkSubmission;
import com.example.pehd.entity.User;
import com.example.pehd.service.HomeworkAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/homework-assignments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HomeworkAssignmentController {

    @Autowired
    private HomeworkAssignmentService homeworkAssignmentService;

    @GetMapping("/my-assignments")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMyAssignments(Authentication authentication) {
        try {
            String studentId = getStudentId(authentication);
            List<Map<String, Object>> assignments = homeworkAssignmentService.getMyAssignments(studentId);
            return ResponseEntity.ok(ApiResponse.success("获取成功", assignments));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取失败: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<ApiResponse<HomeworkSubmission>> submitAssignment(
            @PathVariable String id,
            @RequestBody Map<String, Integer> body,
            Authentication authentication) {
        try {
            String studentId = getStudentId(authentication);
            Integer completedCount = body.get("completedCount");
            if (completedCount == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("completedCount不能为空"));
            }
            HomeworkSubmission submission = homeworkAssignmentService.submitAssignment(id, studentId, completedCount);
            return ResponseEntity.ok(ApiResponse.success("提交成功", submission));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("提交失败: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}/my-submission")
    public ResponseEntity<ApiResponse<HomeworkSubmission>> getMySubmission(
            @PathVariable String id,
            Authentication authentication) {
        try {
            String studentId = getStudentId(authentication);
            HomeworkSubmission submission = homeworkAssignmentService.getMySubmission(id, studentId);
            if (submission == null) {
                return ResponseEntity.ok(ApiResponse.success("暂无提交记录", null));
            }
            return ResponseEntity.ok(ApiResponse.success("获取成功", submission));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取失败: " + e.getMessage()));
        }
    }

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
