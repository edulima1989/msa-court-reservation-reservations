package com.courtreservation.reservations.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.courtreservation.reservations.dto.CreateReservationRequest;
import com.courtreservation.reservations.dto.ReservationResponse;
import com.courtreservation.reservations.exception.ReservationConflictException;
import com.courtreservation.reservations.exception.ReservationValidationException;
import com.courtreservation.reservations.model.ReservationState;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ReservationServiceIntegrationTests {

  @Autowired
  private ReservationService reservationService;

  @Test
  void createAndSearchReservation() {
    CreateReservationRequest request = new CreateReservationRequest(
        LocalDate.now().plusDays(1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0),
        1L,
        5L);

    ReservationResponse created = reservationService.create(request);

    assertThat(created.reservationId()).isNotNull();
    assertThat(created.reservationState()).isEqualTo(ReservationState.ACTIVO);

    List<ReservationResponse> results = reservationService.search(1L, request.reservationDate(), 5L, ReservationState.ACTIVO);
    assertThat(results).hasSize(1);
    assertThat(results.getFirst().reservationId()).isEqualTo(created.reservationId());
  }

  @Test
  void findsOnlyActiveReservationsForDateOrderedByStartTime() {
    LocalDate date = LocalDate.now().plusDays(3);
    ReservationResponse laterReservation = reservationService.create(new CreateReservationRequest(
        date,
        LocalTime.of(11, 0),
        LocalTime.of(12, 0),
        10L,
        5L));
    ReservationResponse earlyReservation = reservationService.create(new CreateReservationRequest(
        date,
        LocalTime.of(8, 0),
        LocalTime.of(9, 0),
        11L,
        6L));
    ReservationResponse canceledReservation = reservationService.create(new CreateReservationRequest(
        date,
        LocalTime.of(9, 0),
        LocalTime.of(10, 0),
        12L,
        7L));
    reservationService.cancel(canceledReservation.reservationId());
    reservationService.create(new CreateReservationRequest(
        date.plusDays(1),
        LocalTime.of(8, 0),
        LocalTime.of(9, 0),
        13L,
        8L));

    List<ReservationResponse> results = reservationService.getActiveByDate(date);

    assertThat(results)
        .extracting(ReservationResponse::reservationId)
        .containsExactly(earlyReservation.reservationId(), laterReservation.reservationId());
    assertThat(results)
        .allMatch(reservation -> reservation.reservationState() == ReservationState.ACTIVO);
  }

  @Test
  void rejectsNullDateWhenFindingActiveReservations() {
    assertThatThrownBy(() -> reservationService.getActiveByDate(null))
        .isInstanceOf(ReservationValidationException.class)
        .hasMessage("La fecha es obligatoria");
  }

  @Test
  void preventsOverlappingReservationsForSameCourt() {
    CreateReservationRequest first = new CreateReservationRequest(
        LocalDate.now().plusDays(1),
        LocalTime.of(9, 0),
        LocalTime.of(10, 0),
        2L,
        5L);
    reservationService.create(first);

    CreateReservationRequest conflicting = new CreateReservationRequest(
        first.reservationDate(),
        LocalTime.of(9, 30),
        LocalTime.of(10, 30),
        2L,
        8L);

    assertThatThrownBy(() -> reservationService.create(conflicting))
        .isInstanceOf(ReservationConflictException.class);
  }

  @Test
  void cancelsReservation() {
    CreateReservationRequest request = new CreateReservationRequest(
        LocalDate.now().plusDays(2),
        LocalTime.of(11, 0),
        LocalTime.of(12, 0),
        3L,
        9L);

    ReservationResponse created = reservationService.create(request);
    ReservationResponse canceled = reservationService.cancel(created.reservationId());

    assertThat(canceled.reservationState()).isEqualTo(ReservationState.CANCELADO);
  }

  @Test
  void rejectsInvalidTimeRange() {
    CreateReservationRequest invalid = new CreateReservationRequest(
        LocalDate.now().plusDays(1),
        LocalTime.of(12, 0),
        LocalTime.of(11, 0),
        4L,
        9L);

    assertThatThrownBy(() -> reservationService.create(invalid))
        .isInstanceOf(ReservationValidationException.class);
  }
}
