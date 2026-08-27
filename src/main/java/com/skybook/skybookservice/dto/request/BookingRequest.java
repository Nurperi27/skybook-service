package com.skybook.skybookservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookingRequest(@NotNull(message = "ID рейса не должен быть пустым") Long flightId,
                             @NotNull(message = "Имя пассажира не должен быть пустым") String passengerName,
                             @NotBlank(message = "Номер места не должен быть пустым") String seatNumber) {}
