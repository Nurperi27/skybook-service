package com.skybook.skybookservice.controller;

import com.skybook.skybookservice.dto.response.FlightResponse;
import com.skybook.skybookservice.entity.Flight;
import com.skybook.skybookservice.repository.FlightRepository;
import com.skybook.skybookservice.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;

    @GetMapping("/search")
    public List<FlightResponse> searchFlights(@RequestParam String origin, @RequestParam String destination, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date) {
        return flightService.searchFlights(origin, destination, date);
    }
}
