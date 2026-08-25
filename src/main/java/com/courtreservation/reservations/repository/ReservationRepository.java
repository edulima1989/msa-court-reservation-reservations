package com.courtreservation.reservations.repository;

import com.courtreservation.reservations.model.Reservation;
import com.courtreservation.reservations.model.ReservationState;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  List<Reservation> findByReservationDateAndReservationStateOrderByReservationStartTime(
      LocalDate reservationDate,
      ReservationState reservationState);

  List<Reservation> findByReservationStateOrderByReservationDateAscReservationStartTimeAsc(
      ReservationState reservationState);

  List<Reservation> findByReservationUserIdAndReservationStateOrderByReservationDateAscReservationStartTimeAsc(
      Long reservationUserId,
      ReservationState reservationState);

  @Query("""
      select r
      from Reservation r
      where r.reservationCourtId = :courtId
        and r.reservationDate = :reservationDate
        and r.reservationState = com.courtreservation.reservations.model.ReservationState.ACTIVO
        and r.reservationStartTime < :reservationEndTime
        and r.reservationEndTime > :reservationStartTime
      order by r.reservationStartTime
      """)
  List<Reservation> findConflictingActiveReservations(
      @Param("courtId") Long courtId,
      @Param("reservationDate") LocalDate reservationDate,
      @Param("reservationStartTime") LocalTime reservationStartTime,
      @Param("reservationEndTime") LocalTime reservationEndTime);

  @Query("""
      select r
      from Reservation r
      where (:reservationCourtId is null or r.reservationCourtId = :reservationCourtId)
        and (:reservationDate is null or r.reservationDate = :reservationDate)
        and (:reservationUserId is null or r.reservationUserId = :reservationUserId)
        and (:reservationState is null or r.reservationState = :reservationState)
      order by r.reservationDate, r.reservationStartTime
      """)
  List<Reservation> search(
      @Param("reservationCourtId") Long reservationCourtId,
      @Param("reservationDate") LocalDate reservationDate,
      @Param("reservationUserId") Long reservationUserId,
      @Param("reservationState") ReservationState reservationState);

  @Query("""
      select count(r)
      from Reservation r
      where r.reservationUserId = :reservationUserId
        and r.reservationState = com.courtreservation.reservations.model.ReservationState.ACTIVO
        and (r.reservationDate > :currentDate
          or (r.reservationDate = :currentDate and r.reservationEndTime > :currentTime))
      """)
  long countActiveNonExpiredReservationsByUserId(
      @Param("reservationUserId") Long reservationUserId,
      @Param("currentDate") LocalDate currentDate,
      @Param("currentTime") LocalTime currentTime);
}
