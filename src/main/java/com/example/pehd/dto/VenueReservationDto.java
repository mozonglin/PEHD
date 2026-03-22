package com.example.pehd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class VenueReservationDto {

    private String id;

    @JsonProperty("venue_id")
    private String venueId;

    @JsonProperty("venue_name")
    private String venueName;

    @JsonProperty("venue_type")
    private String venueType;

    @JsonProperty("venue_location")
    private String venueLocation;

    @JsonProperty("booker_id")
    private String bookerId;

    @JsonProperty("booker_name")
    private String bookerName;

    @JsonProperty("booker_phone")
    private String bookerPhone;

    private String purpose;

    @JsonProperty("reservation_date")
    private LocalDate reservationDate;

    @JsonProperty("start_time")
    private LocalTime startTime;

    @JsonProperty("end_time")
    private LocalTime endTime;

    @JsonProperty("people_count")
    private Integer peopleCount;

    private String status;

    @JsonProperty("approved_by_name")
    private String approvedByName;

    @JsonProperty("approved_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;

    @JsonProperty("reject_reason")
    private String rejectReason;

    private String remark;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public VenueReservationDto() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getVenueId() { return venueId; }
    public void setVenueId(String venueId) { this.venueId = venueId; }

    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }

    public String getVenueType() { return venueType; }
    public void setVenueType(String venueType) { this.venueType = venueType; }

    public String getVenueLocation() { return venueLocation; }
    public void setVenueLocation(String venueLocation) { this.venueLocation = venueLocation; }

    public String getBookerId() { return bookerId; }
    public void setBookerId(String bookerId) { this.bookerId = bookerId; }

    public String getBookerName() { return bookerName; }
    public void setBookerName(String bookerName) { this.bookerName = bookerName; }

    public String getBookerPhone() { return bookerPhone; }
    public void setBookerPhone(String bookerPhone) { this.bookerPhone = bookerPhone; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public LocalDate getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public Integer getPeopleCount() { return peopleCount; }
    public void setPeopleCount(Integer peopleCount) { this.peopleCount = peopleCount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getApprovedByName() { return approvedByName; }
    public void setApprovedByName(String approvedByName) { this.approvedByName = approvedByName; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }

    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
