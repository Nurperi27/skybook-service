package com.skybook.skybookservice.dto.response;

import com.skybook.skybookservice.enums.BookingStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BookingResponse(Long id, Long flightId, String passengerName, String seatNumber,
                              BookingStatus bookingStatus, LocalDateTime createdAt) {}
