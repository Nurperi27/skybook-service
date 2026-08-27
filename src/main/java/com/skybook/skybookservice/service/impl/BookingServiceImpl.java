package com.skybook.skybookservice.service.impl;

import com.skybook.skybookservice.dto.request.BookingRequest;
import com.skybook.skybookservice.dto.response.BookingResponse;
import com.skybook.skybookservice.entity.User;
import com.skybook.skybookservice.enums.BookingStatus;
import com.skybook.skybookservice.exceptions.NoAvailableSeatsException;
import com.skybook.skybookservice.exceptions.ResourceNotFoundException;
import com.skybook.skybookservice.exceptions.SeatAlreadyTakenException;
import com.skybook.skybookservice.models.Booking;
import com.skybook.skybookservice.repository.FlightRepository;
import com.skybook.skybookservice.repository.UserRepository;
import com.skybook.skybookservice.repository.template.BookingRepository;
import com.skybook.skybookservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final com.skybook.skybookservice.repository.template.BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(!flightRepository.existsById(bookingRequest.flightId())) throw new ResourceNotFoundException("Рейс не найден");

        //проверяем на уникальность места
        if(bookingRepository.existsByFlightIdAndSeatNumber(bookingRequest.flightId(), bookingRequest.seatNumber())) throw new SeatAlreadyTakenException();

        //уменьшаем available_seat атомарно
        int update = flightRepository.decrementAvailableSeats(bookingRequest.flightId());
        if(update==0) throw new NoAvailableSeatsException();

        //create booking
        Booking booking = Booking.builder().userId(user.getId()).flightId(bookingRequest.flightId()).passengerName(bookingRequest.passengerName()).seatNumber(bookingRequest.seatNumber()).build();
        Booking savedBooking = bookingRepository.save(booking);
        return toResponse(savedBooking);
    }

    private BookingResponse toResponse(Booking savedBooking) {
        return BookingResponse.builder().id(savedBooking.getId()).flightId(savedBooking.getFlightId())
                .passengerName(savedBooking.getPassengerName()).seatNumber(savedBooking.getSeatNumber())
                .bookingStatus(savedBooking.getStatus()).createdAt(savedBooking.getCreatedAt()).build();
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<BookingResponse> getUser_sBookingsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return bookingRepository.findAllBookingByUserId(user.getId()).stream().map(this::toResponse).toList();
    }

    @Override
    public void cancelBooking(Long bookingId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        //проверяем, что этот брон принадлежит пользователю
        if(!booking.getUserId().equals(user.getId())) throw new ResourceNotFoundException("User's booking not found");
        bookingRepository.updateStatus(bookingId, BookingStatus.CANCELLED);
        flightRepository.incrementAvailableSeats(booking.getFlightId());
    }
}
