package com.skybook.skybookservice.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightPatchUpdateRequest(
        String flightNumber,
        String origin,
        String destination,
        @Future(message = "Дата вылета должна быть в будущем") LocalDateTime departureAt,
        @Future(message = "Дата прилёта должна быть в будущем") LocalDateTime arrivalAt,
        @Positive(message = "Цена должна быть больше нуля") BigDecimal price,
        @Min(value = 1, message = "Количество мест должно быть минимум 1") Integer totalSeats) {}
