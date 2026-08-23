package com.courtreservation.reservations.service;

import com.courtreservation.reservations.dto.CreateReservationRequest;
import com.courtreservation.reservations.dto.ReservationResponse;
import com.courtreservation.reservations.model.ReservationState;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

  ReservationResponse create(CreateReservationRequest request);

  ReservationResponse getById(Long reservationId);

  List<ReservationResponse> getActiveByDate(LocalDate date);

  List<ReservationResponse> getAllActive();

  List<ReservationResponse> getActiveByUserId(Long userId);

  List<ReservationResponse> search(Long reservationCourtId, LocalDate reservationDate, Long reservationUserId, ReservationState reservationState);

  ReservationResponse cancel(Long reservationId);
}
