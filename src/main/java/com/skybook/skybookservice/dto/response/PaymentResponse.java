package com.skybook.skybookservice.dto.response;

import com.skybook.skybookservice.enums.PaymentOption;
import com.skybook.skybookservice.enums.PaymentStatus;
import com.stripe.model.PaymentIntent;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentResponse(Long id, Long bookingId, BigDecimal amount, String currency, String stripePaymentId, PaymentStatus status, LocalDateTime paidAt, PaymentOption paymentOption) {}
