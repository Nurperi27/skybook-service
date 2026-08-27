package com.skybook.skybookservice.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightRequest(@NotBlank(message = "Номер рейса не может быть пустым") String flightNumber,
                            @NotBlank(message = "Город отправления не может быть пустым") String origin,
                            @NotBlank(message = "Город назначения не может быть пустым") String destination,
                            @NotNull(message = "Дата вылета не может быть пустым") @Future(message = "Дата вылета должна быть в будущем")LocalDateTime departureAt,
                            @NotNull(message = "Дата посадки не может быть пустым") @Future(message = "Дата прилета должна быть в будущем") LocalDateTime arrivalAt,
                            @NotNull(message = "Цена не должна быть пустой") @Positive(message = "Цена должна быть больше нуля") BigDecimal price,
                            @NotNull(message = "Количесвтво мест не может быть пустым") @Min(value = 1, message = "Количества мест должно быть минимум 1") Integer totalSeats)
{}
