package com.skybook.skybookservice.dto.request;

import com.skybook.skybookservice.validation.ValidEmail;
import com.skybook.skybookservice.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "Имя не должен быть пустым")
        String name,

        @NotBlank(message = "Email не может быть пустым")
        @ValidEmail
        String email,

        @NotBlank(message = "Пароль не должен бытть пустым")
        @ValidPassword
        String password) {
}
