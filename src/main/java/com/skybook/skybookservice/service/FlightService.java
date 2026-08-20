package com.skybook.skybookservice.service;

import com.skybook.skybookservice.dto.response.FlightResponse;

import java.time.LocalDate;
import java.util.List;

public interface FlightService {
    List<FlightResponse> searchFlights(String origin, String destination, LocalDate date);
}
