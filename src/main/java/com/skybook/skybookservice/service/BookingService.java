package com.skybook.skybookservice.service;

import com.skybook.skybookservice.dto.request.BookingRequest;
import com.skybook.skybookservice.dto.response.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest bookingRequest, String email);
    List<BookingResponse> getUser_sBookingsByEmail(String email);
    void cancelBooking(Long bookingId, String email);
}
