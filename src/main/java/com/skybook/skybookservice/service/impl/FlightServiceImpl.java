package com.skybook.skybookservice.service.impl;

import com.skybook.skybookservice.dto.request.FlightPatchUpdateRequest;
import com.skybook.skybookservice.dto.request.FlightRequest;
import com.skybook.skybookservice.dto.response.FlightResponse;
import com.skybook.skybookservice.entity.Flight;
import com.skybook.skybookservice.enums.FlightStatus;
import com.skybook.skybookservice.exceptions.FlightAlreadyExists;
import com.skybook.skybookservice.exceptions.ResourceNotFoundException;
import com.skybook.skybookservice.repository.FlightRepository;
import com.skybook.skybookservice.service.FlightService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<FlightResponse> searchFlights(String origin, String destination, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return flightRepository.findByOriginAndDestinationAndDepartureAtBetweenAndStatus(origin, destination, startOfDay, endOfDay,
                FlightStatus.SCHEDULED).stream().map(this :: toResponse).toList();
    }

    @Override
    public FlightResponse createFlight(FlightRequest flightRequest) {
        if(flightRepository.existsByFlightNumber(flightRequest.flightNumber())){
            throw new FlightAlreadyExists();
        }
        Flight flight = Flight.builder().flightNumber(flightRequest.flightNumber()).origin(flightRequest.origin())
                .destination(flightRequest.destination()).departureAt(flightRequest.departureAt())
                .arrivalAt(flightRequest.arrivalAt()).price(flightRequest.price()).totalSeats(flightRequest.totalSeats()).availableSeats(flightRequest.totalSeats()).build();
        return toResponse(flightRepository.save(flight));
    }

    @Override
    @Transactional
    public FlightResponse updateFlight(Long id, FlightRequest flightRequest) {
        Flight flight = flightRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Рейс не найден"));
        flight.setFlightNumber(flightRequest.flightNumber());
        flight.setOrigin(flightRequest.origin());
        flight.setDestination(flightRequest.destination());
        flight.setDepartureAt(flightRequest.departureAt());
        flight.setArrivalAt(flightRequest.arrivalAt());
        flight.setPrice(flightRequest.price());
        flight.setTotalSeats(flightRequest.totalSeats());
        return toResponse(flightRepository.save(flight));
    }

    @Override
    @Transactional
    public FlightResponse patchUpdateFlight(Long id, FlightPatchUpdateRequest flightRequest) {
        Flight flight = flightRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Рейс не найден"));
        //ModelMapper пропустит null поля автоматически
        modelMapper.map(flightRequest, flight);
        return toResponse(flightRepository.save(flight));
    }

    private FlightResponse toResponse(Flight flight) {
        return FlightResponse.builder().id(flight.getId()).flightNumber(flight.getFlightNumber()).origin(flight.getOrigin())
                .destination(flight.getDestination()).departureAt(flight.getDepartureAt()).arrivalAt(flight.getArrivalAt())
                .price(flight.getPrice()).availableSeats(flight.getAvailableSeats()).status(flight.getStatus()).build();
    }
}
