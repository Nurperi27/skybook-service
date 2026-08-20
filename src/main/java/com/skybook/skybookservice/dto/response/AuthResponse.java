package com.skybook.skybookservice.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(String message, String token) {
}
