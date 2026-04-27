package com.example.pehd.controller;

import com.example.pehd.dto.ApiResponse;
import com.example.pehd.entity.TempClass;
import com.example.pehd.entity.TempClassEnrollment;
import com.example.pehd.entity.User;
import com.example.pehd.service.TempClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/temp-classes")
@CrossOrigin(origins = "*")
public class TempClassController {

    @Autowired
    private TempClassService tempClassService;

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<TempClass>>> getAvailableClasses(
            @RequestParam String school,
            @RequestParam String semester) {
        try {
            List<TempClass> classes = tempClassService.getAvailableClasses(school, semester);
            return ResponseEntity.ok(ApiResponse.success("获取成功", classes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/enroll")
    public ResponseEntity<ApiResponse<TempClassEnrollment>> enroll(
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            String studentId = user.getStudentId();
            String tempClassId = request.get("tempClassId");
            String school = request.get("school");
            String semester = request.get("semester");

            TempClassEnrollment enrollment = tempClassService.enrollStudent(studentId, tempClassId, school, semester);
            return ResponseEntity.ok(ApiResponse.success("选课成功", enrollment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/my-class")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMyClass(
            @RequestParam String semester,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            String studentId = user.getStudentId();

            Map<String, Object> result = tempClassService.getMyEnrollment(studentId, semester);
            if (result == null) {
                return ResponseEntity.ok(ApiResponse.success("暂无选课记录"));
            }
            return ResponseEntity.ok(ApiResponse.success("获取成功", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/my-enrollment")
    public ResponseEntity<ApiResponse<Void>> cancelEnrollment(
            @RequestParam String semester,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            String studentId = user.getStudentId();

            tempClassService.cancelEnrollment(studentId, semester);
            return ResponseEntity.ok(ApiResponse.success("取消选课成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
