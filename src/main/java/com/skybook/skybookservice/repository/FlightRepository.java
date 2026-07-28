package com.skybook.skybookservice.repository;

import com.skybook.skybookservice.entity.Flight;
import com.skybook.skybookservice.enums.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight,Long> {
    List<Flight> findByOriginAndDestinationAndDepartureAtBetweenAndStatus(String origin, String destination, LocalDateTime from, LocalDateTime to, FlightStatus flightStatus);

    //уменьшение available_seats при бронировании
    @Modifying
    @Query("UPDATE Flight f set f.availableSeats = f.availableSeats - 1 where f.id = :id and f.availableSeats > 0")
    int decrementAvailableSeats(@Param("id") Long id);

    //увеличение available_seats при отмене бронирования
    @Modifying
    @Query("update Flight f set f.availableSeats = f.availableSeats + 1 where f.id = :id")
    void incrementAvailableSeats(@Param("id") Long id);

    boolean existsByFlightNumber(String flightNumber);
}
