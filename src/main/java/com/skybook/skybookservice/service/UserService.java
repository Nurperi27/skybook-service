package com.skybook.skybookservice.service;

import com.skybook.skybookservice.dto.request.LoginRequest;
import com.skybook.skybookservice.dto.request.RegisterRequest;
import com.skybook.skybookservice.dto.response.AuthResponse;
import com.skybook.skybookservice.dto.response.TokenResponse;

public interface UserService {
    AuthResponse register(RegisterRequest registerRequest);
    TokenResponse login(LoginRequest loginRequest);
}
