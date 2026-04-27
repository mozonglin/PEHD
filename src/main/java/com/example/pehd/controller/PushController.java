package com.example.pehd.controller;

import com.example.pehd.dto.ApiResponse;
import com.example.pehd.entity.JpushDevice;
import com.example.pehd.entity.User;
import com.example.pehd.repository.JpushDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/push")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PushController {

    @Autowired
    private JpushDeviceRepository jpushDeviceRepository;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @RequestBody Map<String, String> body,
            Authentication authentication) {
        try {
            String studentId = getUserStudentId(authentication);
            String registrationId = body.get("registrationId");
            String platform = body.get("platform");

            if (registrationId == null || platform == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "registrationId和platform不能为空"));
            }

            Optional<JpushDevice> existing = jpushDeviceRepository
                    .findByStudentIdAndPlatform(studentId, platform);

            if (existing.isPresent()) {
                JpushDevice device = existing.get();
                device.setRegistrationId(registrationId);
                device.setIsActive(true);
                jpushDeviceRepository.save(device);
            } else {
                JpushDevice device = new JpushDevice();
                device.setStudentId(studentId);
                device.setRegistrationId(registrationId);
                device.setPlatform(platform);
                device.setIsActive(true);
                jpushDeviceRepository.save(device);
            }

            return ResponseEntity.ok(new ApiResponse<>(true, "设备注册成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @DeleteMapping("/unregister")
    public ResponseEntity<ApiResponse<Void>> unregister(Authentication authentication) {
        try {
            String studentId = getUserStudentId(authentication);
            User user = (User) authentication.getPrincipal();

            jpushDeviceRepository.findByStudentIdAndPlatform(studentId, "ios")
                    .ifPresent(device -> {
                        device.setIsActive(false);
                        jpushDeviceRepository.save(device);
                    });
            jpushDeviceRepository.findByStudentIdAndPlatform(studentId, "android")
                    .ifPresent(device -> {
                        device.setIsActive(false);
                        jpushDeviceRepository.save(device);
                    });

            return ResponseEntity.ok(new ApiResponse<>(true, "设备注销成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    private String getUserStudentId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getStudentId();
    }
}
