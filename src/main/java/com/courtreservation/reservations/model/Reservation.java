package com.courtreservation.reservations.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_id")
  private Long reservationId;

  @Column(name = "reservation_date", nullable = false)
  private LocalDate reservationDate;

  @Column(name = "reservation_start_time", nullable = false)
  private LocalTime reservationStartTime;

  @Column(name = "reservation_end_time", nullable = false)
  private LocalTime reservationEndTime;

  @Column(name = "reservation_court_id", nullable = false)
  private Long reservationCourtId;

  @Column(name = "reservation_user_id", nullable = false)
  private Long reservationUserId;

  @Enumerated(EnumType.STRING)
  @Column(name = "reservation_state", nullable = false)
  private ReservationState reservationState;
}
