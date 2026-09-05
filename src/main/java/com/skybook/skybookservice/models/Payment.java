package com.skybook.skybookservice.models;

import com.skybook.skybookservice.enums.PaymentOption;
import com.skybook.skybookservice.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    Long id;
    Long bookingId;
    BigDecimal amount;
    String currency;
    String stripePaymentId;
    PaymentStatus status;
    LocalDateTime paidAt;
    PaymentOption paymentOption; //способ оплаты
}
