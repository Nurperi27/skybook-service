package com.skybook.skybookservice.entity;

import com.skybook.skybookservice.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flight_seq")
    @SequenceGenerator(name = "flight_seq", sequenceName = "flight_flight_seq", allocationSize = 1)
    Long id;
    @Column(name = "flight_number", nullable = false)
    String flightNumber;
    @Column(nullable = false)
    String origin;
    @Column(nullable = false)
    String destination;
    @Column(name = "departure_at", nullable = false)
    LocalDateTime departureAt;
    @Column(name = "arrival_at", nullable = false)
    LocalDateTime arrivalAt;
    @Column(nullable = false)
    BigDecimal price;
    @Column(name = "total_seats", nullable = false)
    Integer totalSeats;
    @Column(name = "available_seats", nullable = false)
    Integer availableSeats;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    FlightStatus status;
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        if(this.status == null)
            this.status = FlightStatus.SCHEDULED;
    }
}
