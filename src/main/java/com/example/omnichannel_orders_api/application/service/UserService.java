package com.example.omnichannel_orders_api.application.service;

import com.example.omnichannel_orders_api.application.dto.CreateUserRequest;
import com.example.omnichannel_orders_api.application.dto.UserResponse;
import com.example.omnichannel_orders_api.domain.enums.UserRole;
import com.example.omnichannel_orders_api.domain.model.User;
import com.example.omnichannel_orders_api.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createStaff(CreateUserRequest request) {
        if (request.role() == UserRole.CUSTOMER) {
            throw new IllegalArgumentException("CUSTOMER accounts must be created via /auth/register/customer");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .name(request.name())
                .role(request.role())
                .build();

        return UserResponse.from(userRepository.save(user));
    }
}
