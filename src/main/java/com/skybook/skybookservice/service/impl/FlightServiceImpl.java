package com.skybook.skybookservice.service.impl;

import com.skybook.skybookservice.dto.response.FlightResponse;
import com.skybook.skybookservice.entity.Flight;
import com.skybook.skybookservice.enums.FlightStatus;
import com.skybook.skybookservice.repository.FlightRepository;
import com.skybook.skybookservice.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;

    @Override
    public List<FlightResponse> searchFlights(String origin, String destination, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return flightRepository.findByOriginAndDestinationAndDepartureAtBetweenAndStatus(origin, destination, startOfDay, endOfDay,
                FlightStatus.SCHEDULED).stream().map(this :: toResponse).toList();
    }

    private FlightResponse toResponse(Flight flight) {
        return FlightResponse.builder().id(flight.getId()).flightNumber(flight.getFlightNumber()).origin(flight.getOrigin())
                .destination(flight.getDestination()).departureAt(flight.getDepartureAt()).arrivalAt(flight.getArrivalAt())
                .price(flight.getPrice()).availableSeats(flight.getAvailableSeats()).status(flight.getStatus()).build();
    }
}
