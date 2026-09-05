package com.skybook.skybookservice.dto.request;

import com.skybook.skybookservice.enums.PaymentOption;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(@NotNull(message = "Метод оплаты не может быть пустым") PaymentOption paymentOption) {}
