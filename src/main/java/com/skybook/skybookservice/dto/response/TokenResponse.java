package com.skybook.skybookservice.dto.response;

import lombok.Builder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Builder
public record TokenResponse(String token, String role){}
