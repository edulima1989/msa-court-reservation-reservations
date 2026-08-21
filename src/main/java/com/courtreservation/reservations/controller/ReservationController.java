package com.courtreservation.reservations.controller;

import com.courtreservation.reservations.dto.CreateReservationRequest;
import com.courtreservation.reservations.dto.ReservationResponse;
import com.courtreservation.reservations.model.ReservationState;
import com.courtreservation.reservations.service.ReservationService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

  private final ReservationService reservationService;

  public ReservationController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @PostMapping
  public ResponseEntity<ReservationResponse> create(@RequestBody CreateReservationRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(request));
  }

  @GetMapping("/{reservationId}")
  public ReservationResponse getById(@PathVariable Long reservationId) {
    return reservationService.getById(reservationId);
  }

  @GetMapping
  public List<ReservationResponse> search(
      @RequestParam(required = false) Long reservationCourtId,
      @RequestParam(required = false) LocalDate reservationDate,
      @RequestParam(required = false) Long reservationUserId,
      @RequestParam(required = false) ReservationState reservationState) {
    return reservationService.search(
        reservationCourtId,
        reservationDate,
        reservationUserId,
        reservationState);
  }

  @PatchMapping("/{reservationId}/cancel")
  public ReservationResponse cancel(@PathVariable Long reservationId) {
    return reservationService.cancel(reservationId);
  }
}
