package com.skybook.skybookservice.service.impl;

import com.skybook.skybookservice.dto.request.RegisterRequest;
import com.skybook.skybookservice.dto.response.AuthResponse;
import com.skybook.skybookservice.entity.User;
import com.skybook.skybookservice.enums.UserRole;
import com.skybook.skybookservice.exceptions.EmailAlreadyExistsException;
import com.skybook.skybookservice.repository.UserRepository;
import com.skybook.skybookservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.email())){
            throw new EmailAlreadyExistsException();
        }
        User user = User.builder().name(registerRequest.name()).email(registerRequest.email()).password(passwordEncoder.encode(registerRequest.password())).role(UserRole.USER).isActive(true).build();
        userRepository.save(user);
        return new AuthResponse("Successfully register", user.getId());
    }
}
