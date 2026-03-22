package com.example.pehd.repository;

import com.example.pehd.entity.VenueReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface VenueReservationRepository extends JpaRepository<VenueReservation, String> {

    List<VenueReservation> findByBookerIdOrderByReservationDateDescCreatedAtDesc(String bookerId);

    List<VenueReservation> findByVenueIdAndReservationDateOrderByStartTimeAsc(String venueId, LocalDate date);

    @Query("SELECT COUNT(r) > 0 FROM VenueReservation r WHERE " +
           "r.venueId = :venueId AND r.reservationDate = :date AND " +
           "r.status IN ('pending', 'approved') AND " +
           "r.startTime < :endTime AND r.endTime > :startTime")
    boolean existsConflict(@Param("venueId") String venueId,
                           @Param("date") LocalDate date,
                           @Param("startTime") LocalTime startTime,
                           @Param("endTime") LocalTime endTime);
}
