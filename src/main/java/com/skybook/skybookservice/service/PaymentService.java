package com.skybook.skybookservice.service;

import com.skybook.skybookservice.dto.request.PaymentRequest;
import com.skybook.skybookservice.dto.response.PaymentIntentResponse;
import com.skybook.skybookservice.dto.response.PaymentResponse;
import com.skybook.skybookservice.enums.PaymentOption;
import jakarta.validation.constraints.NotNull;

public interface PaymentService {
    PaymentIntentResponse  createPaymentIntent(Long bookingId, String email, PaymentOption paymentOption);
    PaymentResponse confirmPayment(Long bookingId, String paymentIntentId, String email);

}
