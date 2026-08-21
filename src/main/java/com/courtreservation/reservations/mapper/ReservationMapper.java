package com.courtreservation.reservations.mapper;

import com.courtreservation.reservations.dto.CreateReservationRequest;
import com.courtreservation.reservations.dto.ReservationResponse;
import com.courtreservation.reservations.model.Reservation;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

  @Mapping(target = "reservationId", ignore = true)
  @Mapping(target = "reservationState", ignore = true)
  Reservation toEntity(CreateReservationRequest request);

  ReservationResponse toResponse(Reservation reservation);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "reservationId", ignore = true)
  @Mapping(target = "reservationState", ignore = true)
  void updateFromRequest(CreateReservationRequest request, @MappingTarget Reservation reservation);
}
