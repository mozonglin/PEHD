package com.example.pehd.controller;

import com.example.pehd.dto.ApiResponse;
import com.example.pehd.dto.CreateVenueReservationRequest;
import com.example.pehd.dto.VenueDto;
import com.example.pehd.dto.VenueReservationDto;
import com.example.pehd.service.VenueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 场馆预约控制器（学生端）
 * 路径：/v1/venue
 */
@RestController
@RequestMapping("/v1/venue")
@CrossOrigin(origins = "*")
public class VenueController {

    @Autowired
    private VenueService venueService;

    /**
     * 获取所有可用场馆
     * GET /v1/venue/venues
     */
    @GetMapping("/venues")
    public ResponseEntity<ApiResponse<List<VenueDto>>> getAvailableVenues(Authentication authentication) {
        try {
            String userId = authentication.getName();
            List<VenueDto> venues = venueService.getAvailableVenues(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "获取场馆列表成功", venues));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "获取场馆列表失败: " + e.getMessage(), null));
        }
    }

    /**
     * 获取场馆详情
     * GET /v1/venue/venues/{id}
     */
    @GetMapping("/venues/{id}")
    public ResponseEntity<ApiResponse<VenueDto>> getVenueById(@PathVariable String id) {
        try {
            Optional<VenueDto> venue = venueService.getVenueById(id);
            if (venue.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(true, "获取场馆详情成功", venue.get()));
            } else {
                return ResponseEntity.ok(new ApiResponse<>(false, "场馆不存在", null));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "获取场馆详情失败: " + e.getMessage(), null));
        }
    }

    /**
     * 获取场馆某日预约日历（仅显示待审批和已批准的）
     * GET /v1/venue/venues/{id}/schedule?date=2024-01-01
     */
    @GetMapping("/venues/{id}/schedule")
    public ResponseEntity<ApiResponse<List<VenueReservationDto>>> getVenueSchedule(
            @PathVariable String id,
            @RequestParam String date) {
        try {
            List<VenueReservationDto> schedule = venueService.getVenueSchedule(id, date);
            return ResponseEntity.ok(new ApiResponse<>(true, "获取预约日历成功", schedule));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "获取预约日历失败: " + e.getMessage(), null));
        }
    }

    /**
     * 获取我的预约记录
     * GET /v1/venue/my-reservations
     */
    @GetMapping("/my-reservations")
    public ResponseEntity<ApiResponse<List<VenueReservationDto>>> getMyReservations(
            Authentication authentication) {
        try {
            String userId = authentication.getName();
            List<VenueReservationDto> reservations = venueService.getMyReservations(userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "获取预约记录成功", reservations));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "获取预约记录失败: " + e.getMessage(), null));
        }
    }

    /**
     * 提交场馆预约
     * POST /v1/venue/reservations
     */
    @PostMapping("/reservations")
    public ResponseEntity<ApiResponse<VenueReservationDto>> createReservation(
            @Valid @RequestBody CreateVenueReservationRequest request,
            Authentication authentication) {
        try {
            String userId = authentication.getName();
            VenueReservationDto reservation = venueService.createReservation(request, userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "预约提交成功，等待管理员审批", reservation));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "提交预约失败: " + e.getMessage(), null));
        }
    }

    /**
     * 取消我的预约
     * POST /v1/venue/reservations/{id}/cancel
     */
    @PostMapping("/reservations/{id}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelReservation(
            @PathVariable String id,
            Authentication authentication) {
        try {
            String userId = authentication.getName();
            venueService.cancelReservation(id, userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "预约已取消", null));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "取消预约失败: " + e.getMessage(), null));
        }
    }

    /**
     * 健康检查
     * GET /v1/venue/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(new ApiResponse<>(true, "场馆预约服务运行正常", "OK"));
    }
}
