package com.courtreservation.reservations.dto;

import com.courtreservation.reservations.model.ReservationState;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponse(
    Long reservationId,
    LocalDate reservationDate,
    LocalTime reservationStartTime,
    LocalTime reservationEndTime,
    Long reservationCourtId,
    Long reservationUserId,
    ReservationState reservationState) {
}
