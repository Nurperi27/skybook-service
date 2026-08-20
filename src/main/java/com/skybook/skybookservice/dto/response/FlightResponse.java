package com.skybook.skybookservice.dto.response;

import com.skybook.skybookservice.enums.FlightStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record FlightResponse(Long id, String flightNumber, String origin, String destination,
                             LocalDateTime departureAt, LocalDateTime arrivalAt,
                             BigDecimal price, Integer availableSeats, FlightStatus status) {}
