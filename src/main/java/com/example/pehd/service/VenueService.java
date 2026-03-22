package com.example.pehd.service;

import com.example.pehd.dto.CreateVenueReservationRequest;
import com.example.pehd.dto.VenueDto;
import com.example.pehd.dto.VenueReservationDto;
import com.example.pehd.entity.User;
import com.example.pehd.entity.Venue;
import com.example.pehd.entity.VenueReservation;
import com.example.pehd.repository.UserRepository;
import com.example.pehd.repository.VenueRepository;
import com.example.pehd.repository.VenueReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class VenueService {

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private VenueReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    private String getUserSchool(String studentId) {
        if (studentId == null) return null;
        return userRepository.findByStudentId(studentId).map(u -> u.getSchool()).orElse(null);
    }

    public List<VenueDto> getAvailableVenues(String userId) {
        String school = getUserSchool(userId);
        if (school != null) {
            return venueRepository.findBySchoolAndIsDeletedFalseAndStatus(school, "available")
                    .stream().map(this::toDto).collect(Collectors.toList());
        }
        return venueRepository.findByIsDeletedFalseAndStatus("available")
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<VenueDto> getAllVenues(String userId) {
        String school = getUserSchool(userId);
        if (school != null) {
            return venueRepository.findBySchoolAndIsDeletedFalse(school)
                    .stream().map(this::toDto).collect(Collectors.toList());
        }
        return venueRepository.findByIsDeletedFalse()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<VenueDto> getVenueById(String id) {
        return venueRepository.findByIdAndIsDeletedFalse(id).map(this::toDto);
    }

    public List<VenueReservationDto> getVenueSchedule(String venueId, String date) {
        LocalDate localDate = LocalDate.parse(date);
        return reservationRepository
                .findByVenueIdAndReservationDateOrderByStartTimeAsc(venueId, localDate)
                .stream()
                .filter(r -> List.of("pending", "approved").contains(r.getStatus()))
                .map(r -> toReservationDto(r, true))
                .collect(Collectors.toList());
    }

    public List<VenueReservationDto> getMyReservations(String userId) {
        return reservationRepository
                .findByBookerIdOrderByReservationDateDescCreatedAtDesc(userId)
                .stream()
                .map(r -> toReservationDto(r, false))
                .collect(Collectors.toList());
    }

    @Transactional
    public VenueReservationDto createReservation(CreateVenueReservationRequest req, String userId) {
        Venue venue = venueRepository.findByIdAndIsDeletedFalse(req.getVenueId())
                .orElseThrow(() -> new RuntimeException("场馆不存在"));
        if (!"available".equals(venue.getStatus())) {
            throw new RuntimeException("该场馆当前不可预约");
        }

        LocalDate date = LocalDate.parse(req.getReservationDate());
        LocalTime start = LocalTime.parse(req.getStartTime());
        LocalTime end = LocalTime.parse(req.getEndTime());

        if (!end.isAfter(start)) {
            throw new RuntimeException("结束时间必须晚于开始时间");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new RuntimeException("不能预约过去的日期");
        }
        if (reservationRepository.existsConflict(req.getVenueId(), date, start, end)) {
            throw new RuntimeException("该时间段已有预约，请选择其他时间");
        }

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        VenueReservation reservation = new VenueReservation();
        reservation.setId(UUID.randomUUID().toString());
        reservation.setVenueId(req.getVenueId());
        reservation.setBookerId(userId);
        reservation.setBookerName(booker.getName());
        reservation.setBookerPhone(req.getBookerPhone() != null ? req.getBookerPhone() : booker.getPhoneNumber());
        reservation.setPurpose(req.getPurpose());
        reservation.setReservationDate(date);
        reservation.setStartTime(start);
        reservation.setEndTime(end);
        reservation.setPeopleCount(req.getPeopleCount() != null ? req.getPeopleCount() : 1);
        reservation.setRemark(req.getRemark());
        reservation.setStatus("pending");

        VenueReservation saved = reservationRepository.save(reservation);
        VenueReservationDto dto = toReservationDto(saved, false);
        dto.setVenueName(venue.getName());
        dto.setVenueType(venue.getType());
        dto.setVenueLocation(venue.getLocation());
        return dto;
    }

    @Transactional
    public void cancelReservation(String reservationId, String userId) {
        VenueReservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("预约记录不存在"));
        if (!reservation.getBookerId().equals(userId)) {
            throw new RuntimeException("只能取消自己的预约");
        }
        if ("completed".equals(reservation.getStatus()) || "cancelled".equals(reservation.getStatus())) {
            throw new RuntimeException("该预约已完成或已取消，无法再次取消");
        }
        reservation.setStatus("cancelled");
        reservationRepository.save(reservation);
    }

    private VenueDto toDto(Venue venue) {
        VenueDto dto = new VenueDto();
        dto.setId(venue.getId());
        dto.setName(venue.getName());
        dto.setType(venue.getType());
        dto.setCapacity(venue.getCapacity());
        dto.setLocation(venue.getLocation());
        dto.setPrice(venue.getPrice());
        dto.setOpenTime(venue.getOpenTime());
        dto.setDescription(venue.getDescription());
        dto.setStatus(venue.getStatus());
        dto.setCreatedAt(venue.getCreatedAt());
        return dto;
    }

    private VenueReservationDto toReservationDto(VenueReservation r, boolean includeVenue) {
        VenueReservationDto dto = new VenueReservationDto();
        dto.setId(r.getId());
        dto.setVenueId(r.getVenueId());
        dto.setBookerId(r.getBookerId());
        dto.setBookerName(r.getBookerName());
        dto.setBookerPhone(r.getBookerPhone());
        dto.setPurpose(r.getPurpose());
        dto.setReservationDate(r.getReservationDate());
        dto.setStartTime(r.getStartTime());
        dto.setEndTime(r.getEndTime());
        dto.setPeopleCount(r.getPeopleCount());
        dto.setStatus(r.getStatus());
        dto.setApprovedAt(r.getApprovedAt());
        dto.setRejectReason(r.getRejectReason());
        dto.setRemark(r.getRemark());
        dto.setCreatedAt(r.getCreatedAt());
        if (includeVenue) {
            venueRepository.findByIdAndIsDeletedFalse(r.getVenueId()).ifPresent(v -> {
                dto.setVenueName(v.getName());
                dto.setVenueType(v.getType());
                dto.setVenueLocation(v.getLocation());
            });
        } else {
            venueRepository.findById(r.getVenueId()).ifPresent(v -> {
                dto.setVenueName(v.getName());
                dto.setVenueType(v.getType());
                dto.setVenueLocation(v.getLocation());
            });
        }
        return dto;
    }
}
