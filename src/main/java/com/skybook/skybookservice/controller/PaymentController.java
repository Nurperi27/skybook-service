package com.skybook.skybookservice.controller;

import com.skybook.skybookservice.dto.request.PaymentRequest;
import com.skybook.skybookservice.dto.response.PaymentIntentResponse;
import com.skybook.skybookservice.dto.response.PaymentResponse;
import com.skybook.skybookservice.enums.PaymentOption;
import com.skybook.skybookservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    // 1 - создаем PaymentIntent и возвращаем client_secret фронту
    @PostMapping("/create-intent/{bookingId}")
    public PaymentIntentResponse createPaymentIntent(@PathVariable("bookingId") Long bookingId, @AuthenticationPrincipal UserDetails userDetails, @Valid @RequestParam PaymentOption paymentOption) {
        return paymentService.createPaymentIntent(bookingId, userDetails.getUsername(), paymentOption);
    }

    // 2 - фронт подтверждает оплату через Stripe.js, обновляем статус
    @PostMapping("/confirm/{bookingId}")
    public PaymentResponse confirmPayment(@PathVariable("bookingId") Long bookingId, @RequestParam String paymentIntentId, @AuthenticationPrincipal UserDetails userDetails){
        return paymentService.confirmPayment(bookingId, paymentIntentId, userDetails.getUsername());
    }
}
