package com.skybook.skybookservice.service;

import com.skybook.skybookservice.dto.request.RegisterRequest;
import com.skybook.skybookservice.dto.response.AuthResponse;

public interface UserService {
    AuthResponse register(RegisterRequest registerRequest);
}
