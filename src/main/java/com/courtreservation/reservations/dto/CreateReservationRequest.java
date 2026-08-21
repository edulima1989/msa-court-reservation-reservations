package com.courtreservation.reservations.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos necesarios para crear una reserva")
public record CreateReservationRequest(
    @Schema(description = "Fecha de la reserva", example = "2026-08-22")
    LocalDate reservationDate,
    @Schema(description = "Hora de inicio", example = "09:00:00")
    LocalTime reservationStartTime,
    @Schema(description = "Hora de fin", example = "10:00:00")
    LocalTime reservationEndTime,
    @Schema(description = "Identificador de la cancha", example = "1")
    Long reservationCourtId,
    @Schema(description = "Identificador del usuario", example = "15")
    Long reservationUserId) {
}
