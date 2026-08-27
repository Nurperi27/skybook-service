package com.skybook.skybookservice.controller;

import com.skybook.skybookservice.dto.request.BookingRequest;
import com.skybook.skybookservice.dto.response.BookingResponse;
import com.skybook.skybookservice.repository.template.BookingRepository;
import com.skybook.skybookservice.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponse createBooking(@Valid @RequestBody BookingRequest bookingRequest, @AuthenticationPrincipal UserDetails userDetails) {
        return bookingService.createBooking(bookingRequest, userDetails.getUsername());
    }

    @GetMapping("/get-my-booking")
    public List<BookingResponse> getMyBooking(@AuthenticationPrincipal UserDetails userDetails) {
        return bookingService.getUser_sBookingsByEmail(userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        bookingService.cancelBooking(id, userDetails.getUsername());
    }
}
