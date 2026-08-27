package com.skybook.skybookservice.controller;

import com.skybook.skybookservice.dto.request.FlightPatchUpdateRequest;
import com.skybook.skybookservice.dto.request.FlightRequest;
import com.skybook.skybookservice.dto.response.BookingResponse;
import com.skybook.skybookservice.dto.response.FlightResponse;
import com.skybook.skybookservice.service.BookingService;
import com.skybook.skybookservice.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private  final BookingService  bookingService;
    private final FlightService flightService;

    @GetMapping("/all-bookings")
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @PostMapping("/flights")
    public FlightResponse createFlight(@Valid @RequestBody FlightRequest flightRequest) {
        return flightService.createFlight(flightRequest);
    }

    @PutMapping("/flights/{id}")
    public FlightResponse updateFlight(@PathVariable Long id, @Valid @RequestBody FlightRequest flightRequest) {
        return flightService.updateFlight(id, flightRequest);
    }

    @PatchMapping("/flights/{id}")
    public FlightResponse patchFlight(@PathVariable Long id, @Valid @RequestBody FlightPatchUpdateRequest flightRequest) {
        return flightService.patchUpdateFlight(id, flightRequest);
    }
}
