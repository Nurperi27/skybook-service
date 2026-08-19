package com.skybook.skybookservice.dto.request;

import com.skybook.skybookservice.validation.ValidEmail;
import com.skybook.skybookservice.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message = "Email не должен быть пустым") @ValidEmail String email,
                           @NotBlank(message = "пароль не должен быть пустым") @ValidPassword String password) {}
