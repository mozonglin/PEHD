package com.example.pehd.controller;

import com.example.pehd.dto.ApiResponse;
import com.example.pehd.entity.StudentFitnessProfile;
import com.example.pehd.entity.TrainingTaskRecord;
import com.example.pehd.entity.User;
import com.example.pehd.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/training")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TrainingController {

    @Autowired
    private TrainingService trainingService;

    /** 获取我的体能档案 */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<StudentFitnessProfile>> getMyProfile(Authentication auth) {
        try {
            User user = getUser(auth);
            StudentFitnessProfile profile = trainingService.getOrCreateProfile(user);
            return ResponseEntity.ok(ApiResponse.success("获取成功", profile));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取失败: " + e.getMessage()));
        }
    }

    /** 上传体测数据 */
    @PostMapping("/profile/tice-data")
    public ResponseEntity<ApiResponse<StudentFitnessProfile>> uploadTiceData(
            @RequestBody Map<String, Object> data, Authentication auth) {
        try {
            User user = getUser(auth);
            StudentFitnessProfile profile = trainingService.uploadTiceData(user, data);
            return ResponseEntity.ok(ApiResponse.success("上传成功", profile));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("上传失败: " + e.getMessage()));
        }
    }

    /** 获取我的待完成训练任务 */
    @GetMapping("/my-tasks")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMyTasks(Authentication auth) {
        try {
            User user = getUser(auth);
            List<Map<String, Object>> tasks = trainingService.getStudentActiveTasks(user.getId());
            return ResponseEntity.ok(ApiResponse.success("获取成功", tasks));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取失败: " + e.getMessage()));
        }
    }

    /** 更新训练任务进度 */
    @PostMapping("/tasks/{taskId}/progress")
    public ResponseEntity<ApiResponse<TrainingTaskRecord>> updateProgress(
            @PathVariable Long taskId,
            @RequestBody Map<String, Object> body,
            Authentication auth) {
        try {
            User user = getUser(auth);
            String completionData = body.get("completionData") != null ? body.get("completionData").toString() : null;
            BigDecimal completionRate = body.get("completionRate") != null ?
                    new BigDecimal(body.get("completionRate").toString()) : BigDecimal.ZERO;
            TrainingTaskRecord record = trainingService.updateTaskProgress(
                    user.getId(), taskId, completionData, completionRate);
            return ResponseEntity.ok(ApiResponse.success("更新成功", record));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("更新失败: " + e.getMessage()));
        }
    }

    private User getUser(Authentication auth) {
        if (auth == null || auth.getPrincipal() == null) throw new RuntimeException("未登录");
        Object principal = auth.getPrincipal();
        if (principal instanceof User) return (User) principal;
        throw new RuntimeException("无效的认证信息");
    }
}
