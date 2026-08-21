package com.courtreservation.reservations.exception;

public class ReservationConflictException extends RuntimeException {

  public ReservationConflictException(String message) {
    super(message);
  }
}
