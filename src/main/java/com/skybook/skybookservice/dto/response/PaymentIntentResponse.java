package com.skybook.skybookservice.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentIntentResponse(String clientSecret, String PaymentIntentId, BigDecimal amount, String currency) {}
