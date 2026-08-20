package com.skybook.skybookservice.service.impl;

import com.skybook.skybookservice.config.security.JwtUtil;
import com.skybook.skybookservice.dto.request.LoginRequest;
import com.skybook.skybookservice.dto.request.RegisterRequest;
import com.skybook.skybookservice.dto.response.AuthResponse;
import com.skybook.skybookservice.dto.response.TokenResponse;
import com.skybook.skybookservice.entity.User;
import com.skybook.skybookservice.enums.UserRole;
import com.skybook.skybookservice.exceptions.EmailAlreadyExistsException;
import com.skybook.skybookservice.repository.UserRepository;
import com.skybook.skybookservice.service.UserService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, @Lazy AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.email())){
            throw new EmailAlreadyExistsException();
        }
        User user = User.builder().name(registerRequest.name()).email(registerRequest.email()).password(passwordEncoder.encode(registerRequest.password())).role(UserRole.USER).isActive(true).build();
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return AuthResponse.builder().message("Successfully register").token(token).build();
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream().findFirst().map(a -> a.getAuthority().replace("ROLE_", "")).orElse("USER");
        String token = jwtUtil.generateToken(userDetails.getUsername(), role);
        return TokenResponse.builder().token(token).role(role).build();
    }
}
