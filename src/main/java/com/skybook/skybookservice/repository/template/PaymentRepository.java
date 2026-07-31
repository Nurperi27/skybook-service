package com.skybook.skybookservice.repository.template;

import com.skybook.skybookservice.enums.PaymentStatus;
import com.skybook.skybookservice.models.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Payment> paymentRowMapper = (rs, rowNum) -> Payment.builder()
            .id(rs.getLong("id")).bookingId(rs.getLong("booking_id")).amount(rs.getBigDecimal("amount"))
            .currency(rs.getString("currency")).stripePaymentId(rs.getString("stripe_payment_id"))
            .status(PaymentStatus.valueOf(rs.getString("status"))).paidAt(rs.getObject("paid_at", LocalDateTime.class))
            .build();

    public Payment save(Payment payment) {
        String sql = """
                insert into Payment (booking_id, amount, currency, stripe_payment_id, status) values (?, ?, ?, ?, ?);
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, payment.getBookingId());
            ps.setBigDecimal(2, payment.getAmount());
            ps.setString(3, payment.getCurrency() != null ? payment.getCurrency() : "USD");
            ps.setString(4, payment.getStripePaymentId());
            ps.setString(5, PaymentStatus.PENDING.name());
            return ps;
        }, keyHolder);
        payment.setId(((Number) keyHolder.getKeys().get("id")).longValue());
        payment.setStatus(PaymentStatus.PENDING);
        return payment;
    }

    public Optional<Payment> findById(Long id) {
        String sql = "select * from payment where id = ?;";
        return jdbcTemplate.query(sql, paymentRowMapper, id).stream().findFirst();
    }

    public Optional<Payment> findByBookingId(Long bookingId) {
        String sql = "select * from payment where booking_id = ?;";
        return jdbcTemplate.query(sql, paymentRowMapper, bookingId).stream().findFirst();
    }

    //Обновление после успешной оплаты через Stripe
    public void confirmPayment(String id, String stripePaymentId) {
        String sql = "update payments set status = ?, stripe_payment_id = ?, paid_at = ? where id = ?;";
        jdbcTemplate.update(sql, PaymentStatus.CONFIRMED.name(), stripePaymentId, LocalDateTime.now(), id);
    }

    public void updatePaymentStatus(Long paymentId, PaymentStatus paymentStatus) {
        String sql = "update payments set status = ? where id = ?;";
        jdbcTemplate.update(sql, paymentStatus.name(), paymentId);
    }
}
