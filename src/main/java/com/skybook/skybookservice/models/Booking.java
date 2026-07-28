package com.skybook.skybookservice.models;

import com.skybook.skybookservice.enums.BookingStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    Long id;
    Long userId;
    Long flightId;
    String passengerName;
    String seatNumber;
    BookingStatus status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
