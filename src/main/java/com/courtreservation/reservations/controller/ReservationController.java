package com.courtreservation.reservations.controller;

import com.courtreservation.reservations.dto.CreateReservationRequest;
import com.courtreservation.reservations.dto.ReservationResponse;
import com.courtreservation.reservations.model.ReservationState;
import com.courtreservation.reservations.service.ReservationService;
import java.time.LocalDate;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
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
@Tag(name = "Reservas", description = "Creación, consulta y cancelación de reservas")
public class ReservationController {

  private final ReservationService reservationService;

  public ReservationController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @PostMapping
  @Operation(summary = "Crear una reserva")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Reserva creada"),
      @ApiResponse(responseCode = "400", description = "Datos inválidos"),
      @ApiResponse(responseCode = "409", description = "Cancha no disponible")
  })
  public ResponseEntity<ReservationResponse> create(@RequestBody CreateReservationRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(request));
  }

  @GetMapping("/{reservationId}")
  @Operation(summary = "Consultar una reserva por id")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
      @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
  })
  public ReservationResponse getById(@PathVariable Long reservationId) {
    return reservationService.getById(reservationId);
  }

  @GetMapping("/active")
  @Operation(summary = "Consultar reservas activas por fecha")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Reservas activas encontradas"),
      @ApiResponse(responseCode = "400", description = "Fecha inválida o no enviada")
  })
  public List<ReservationResponse> getActiveByDate(
      @Parameter(description = "Fecha de las reservas", example = "2026-08-23", required = true)
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return reservationService.getActiveByDate(date);
  }

  @GetMapping("/active/all")
  @Operation(summary = "Consultar todas las reservas activas")
  @ApiResponse(responseCode = "200", description = "Reservas activas encontradas")
  public List<ReservationResponse> getAllActive() {
    return reservationService.getAllActive();
  }

  @GetMapping("/active/user/{userId}")
  @Operation(summary = "Consultar reservas activas por usuario")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Reservas activas del usuario encontradas"),
      @ApiResponse(responseCode = "400", description = "Identificador de usuario inválido")
  })
  public List<ReservationResponse> getActiveByUserId(
      @Parameter(description = "Identificador del usuario", example = "15", required = true)
      @PathVariable Long userId) {
    return reservationService.getActiveByUserId(userId);
  }

  @GetMapping("/canceled/all")
  @Operation(summary = "Consultar todas las reservas canceladas")
  @ApiResponse(responseCode = "200", description = "Reservas canceladas encontradas")
  public List<ReservationResponse> getAllCanceled() {
    return reservationService.getAllCanceled();
  }

  @GetMapping("/canceled/user/{userId}")
  @Operation(summary = "Consultar reservas canceladas por usuario")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Reservas canceladas del usuario encontradas"),
      @ApiResponse(responseCode = "400", description = "Identificador de usuario inválido")
  })
  public List<ReservationResponse> getCanceledByUserId(
      @Parameter(description = "Identificador del usuario", example = "15", required = true)
      @PathVariable Long userId) {
    return reservationService.getCanceledByUserId(userId);
  }

  @GetMapping
  @Operation(summary = "Buscar reservas")
  public List<ReservationResponse> search(
      @Parameter(description = "Id de la cancha")
      @RequestParam(required = false) Long reservationCourtId,
      @Parameter(description = "Fecha de la reserva")
      @RequestParam(required = false) LocalDate reservationDate,
      @Parameter(description = "Id del usuario")
      @RequestParam(required = false) Long reservationUserId,
      @Parameter(description = "Estado de la reserva")
      @RequestParam(required = false) ReservationState reservationState) {
    return reservationService.search(
        reservationCourtId,
        reservationDate,
        reservationUserId,
        reservationState);
  }

  @PatchMapping("/{reservationId}/cancel")
  @Operation(summary = "Cancelar una reserva")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Reserva cancelada"),
      @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
      @ApiResponse(responseCode = "400", description = "La reserva no puede cancelarse")
  })
  public ReservationResponse cancel(@PathVariable Long reservationId) {
    return reservationService.cancel(reservationId);
  }
}
