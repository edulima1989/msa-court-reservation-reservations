package com.courtreservation.reservations.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateReservationRequest(
    LocalDate reservationDate,
    LocalTime reservationStartTime,
    LocalTime reservationEndTime,
    Long reservationCourtId,
    Long reservationUserId) {
}
