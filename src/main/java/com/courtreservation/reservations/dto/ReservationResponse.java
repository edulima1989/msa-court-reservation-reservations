package com.courtreservation.reservations.dto;

import com.courtreservation.reservations.model.ReservationState;
import java.time.LocalDate;
import java.time.LocalTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de una reserva")
public record ReservationResponse(
    @Schema(description = "Identificador de la reserva", example = "100")
    Long reservationId,
    @Schema(description = "Fecha de la reserva", example = "2026-08-22")
    LocalDate reservationDate,
    @Schema(description = "Hora de inicio", example = "09:00:00")
    LocalTime reservationStartTime,
    @Schema(description = "Hora de fin", example = "10:00:00")
    LocalTime reservationEndTime,
    @Schema(description = "Identificador de la cancha", example = "1")
    Long reservationCourtId,
    @Schema(description = "Identificador del usuario", example = "15")
    Long reservationUserId,
    @Schema(description = "Estado de la reserva", example = "ACTIVO")
    ReservationState reservationState) {
}
