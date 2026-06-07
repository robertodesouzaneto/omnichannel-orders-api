package com.example.omnichannel_orders_api.infrastructure.config;

import com.example.omnichannel_orders_api.domain.enums.UserRole;
import com.example.omnichannel_orders_api.domain.model.User;
import com.example.omnichannel_orders_api.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.bootstrap.email}")
    private String adminEmail;

    @Value("${admin.bootstrap.password}")
    private String adminPassword;

    @Value("${admin.bootstrap.name}")
    private String adminName;

    @Override
    public void run(ApplicationArguments args) {
        boolean adminExists = userRepository.existsByRole(UserRole.ADMIN);

        if (!adminExists) {
            User admin = User.builder()
                    .email(adminEmail)
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .name(adminName)
                    .role(UserRole.ADMIN)
                    .build();

            userRepository.save(admin);
            log.info("Bootstrap admin created: {}", adminEmail);
        }
    }
}
