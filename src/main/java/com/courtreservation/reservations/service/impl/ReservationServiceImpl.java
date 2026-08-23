package com.courtreservation.reservations.service.impl;

import com.courtreservation.reservations.dto.CreateReservationRequest;
import com.courtreservation.reservations.dto.ReservationResponse;
import com.courtreservation.reservations.exception.ReservationConflictException;
import com.courtreservation.reservations.exception.ReservationNotFoundException;
import com.courtreservation.reservations.exception.ReservationValidationException;
import com.courtreservation.reservations.mapper.ReservationMapper;
import com.courtreservation.reservations.model.Reservation;
import com.courtreservation.reservations.model.ReservationState;
import com.courtreservation.reservations.repository.ReservationRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import com.courtreservation.reservations.service.ReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

  private final ReservationRepository reservationRepository;
  private final ReservationMapper reservationMapper;
  private final Clock clock;

  public ReservationServiceImpl(
      ReservationRepository reservationRepository,
      ReservationMapper reservationMapper,
      Clock clock) {
    this.reservationRepository = reservationRepository;
    this.reservationMapper = reservationMapper;
    this.clock = clock;
  }

  @Override
  public ReservationResponse create(CreateReservationRequest request) {
    validateRequest(request);
    ensureNoConflict(request);

    Reservation reservation = reservationMapper.toEntity(request);
    reservation.setReservationState(ReservationState.ACTIVO);

    return reservationMapper.toResponse(reservationRepository.save(reservation));
  }

  @Override
  @Transactional(readOnly = true)
  public ReservationResponse getById(Long reservationId) {
    return reservationMapper.toResponse(findReservation(reservationId));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReservationResponse> getActiveByDate(LocalDate date) {
    if (date == null) {
      throw new ReservationValidationException("La fecha es obligatoria");
    }

    return reservationRepository
        .findByReservationDateAndReservationStateOrderByReservationStartTime(
            date,
            ReservationState.ACTIVO)
        .stream()
        .map(reservationMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReservationResponse> getAllActive() {
    return reservationRepository
        .findByReservationStateOrderByReservationDateAscReservationStartTimeAsc(
            ReservationState.ACTIVO)
        .stream()
        .map(reservationMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReservationResponse> getActiveByUserId(Long userId) {
    if (userId == null) {
      throw new ReservationValidationException("El identificador del usuario es obligatorio");
    }

    return reservationRepository
        .findByReservationUserIdAndReservationStateOrderByReservationDateAscReservationStartTimeAsc(
            userId,
            ReservationState.ACTIVO)
        .stream()
        .map(reservationMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReservationResponse> search(Long reservationCourtId, LocalDate reservationDate, Long reservationUserId, ReservationState reservationState) {
    return reservationRepository.search(reservationCourtId, reservationDate, reservationUserId, reservationState)
        .stream()
        .map(reservationMapper::toResponse)
        .toList();
  }

  @Override
  public ReservationResponse cancel(Long reservationId) {
    Reservation reservation = findReservation(reservationId);
    if (reservation.getReservationState() == ReservationState.CANCELADO) {
      throw new ReservationValidationException("La reserva ya está cancelada");
    }

    reservation.setReservationState(ReservationState.CANCELADO);
    return reservationMapper.toResponse(reservationRepository.save(reservation));
  }

  private void validateRequest(CreateReservationRequest request) {
    if (request == null) {
      throw new ReservationValidationException("La reserva es obligatoria");
    }
    if (request.reservationDate() == null
        || request.reservationStartTime() == null
        || request.reservationEndTime() == null
        || request.reservationCourtId() == null
        || request.reservationUserId() == null) {
      throw new ReservationValidationException("Todos los campos de la reserva son obligatorios");
    }
    if (!request.reservationEndTime().isAfter(request.reservationStartTime())) {
      throw new ReservationValidationException("La hora de fin debe ser posterior a la hora de inicio");
    }
    if (request.reservationDate().isBefore(LocalDate.now(clock))) {
      throw new ReservationValidationException("No se puede reservar en una fecha pasada");
    }
  }

  private void ensureNoConflict(CreateReservationRequest request) {
    if (!reservationRepository.findConflictingActiveReservations(
        request.reservationCourtId(),
        request.reservationDate(),
        request.reservationStartTime(),
        request.reservationEndTime()).isEmpty()) {
      throw new ReservationConflictException("La cancha no está disponible en el horario solicitado");
    }
  }

  private Reservation findReservation(Long reservationId) {
    if (reservationId == null) {
      throw new ReservationValidationException("El identificador de la reserva es obligatorio");
    }
    return reservationRepository.findById(reservationId)
        .orElseThrow(() -> new ReservationNotFoundException("No existe la reserva con id " + reservationId));
  }

}
