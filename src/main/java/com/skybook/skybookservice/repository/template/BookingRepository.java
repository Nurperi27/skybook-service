package com.skybook.skybookservice.repository.template;

import com.skybook.skybookservice.enums.BookingStatus;
import com.skybook.skybookservice.models.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookingRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Booking> bookingRowMapper = (rs, rowNum) -> Booking.builder()
            .id(rs.getLong("id")).userId(rs.getLong("user_id")).flightId(rs.getLong("flight_id"))
            .passengerName(rs.getString("passenger_name")).seatNumber(rs.getString("seat_number"))
            .status(BookingStatus.valueOf(rs.getString("status"))).createdAt(rs.getObject("created_at", LocalDateTime.class))
            .updatedAt(rs.getObject("updated_at", LocalDateTime.class)).build();

    public Booking save(Booking booking) {
        String sql = """
                insert into bookings (user_id, flight_id, passenger_name, seat_number, status, created_at, updated_at) values (?, ?, ?, ?, ?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, booking.getUserId());
            ps.setLong(2, booking.getFlightId());
            ps.setString(3, booking.getPassengerName());
            ps.setString(4, booking.getSeatNumber());
            ps.setString(5, BookingStatus.PENDING.name());
            ps.setObject(6, now);
            ps.setObject(7, now);
            return ps;
        }, keyHolder);
        booking.setId(((Number) keyHolder.getKeys().get("id")).longValue());
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);
        return booking;
    }

    public Optional<Booking> findById(Long id) {
        String sql = "select * from bookings where id = ?";
        return jdbcTemplate.query(sql, bookingRowMapper, id).stream().findFirst();
    }

    //все бронирования конкретного пользователя
    public List<Booking> findAllBookingByUserId(Long userId) {
        String sql = "select * from bookings where user_id = ? order by created_at desc";
        return jdbcTemplate.query(sql, bookingRowMapper, userId);
    }

    //все бронирования
    public List<Booking> findAll() {
        String sql = "select * from bookings order by created_at desc";
        return jdbcTemplate.query(sql, bookingRowMapper);
    }

    //обновление статуса брони
    public void updateStatus(Long id, BookingStatus bookingStatus) {
        String sql = "update bookings set status = ?, updated_at = ? where id = ?";
        jdbcTemplate.update(sql, bookingStatus.name(), LocalDateTime.now(), id);
    }

    //проверка уникальности места на рейсе
    public boolean existsByFlightIdAndSeatNumber(Long flightId, String seatNumber) {
        String sql = "select count(*) from bookings where flight_id = ? and seat_number = ? and status != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, flightId, seatNumber, BookingStatus.CANCELLED.name());
        return count != null && count > 0;
    }
}
