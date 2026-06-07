package com.example.omnichannel_orders_api.infrastructure.repository;

import com.example.omnichannel_orders_api.domain.enums.UserRole;
import com.example.omnichannel_orders_api.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByRole(UserRole role);
}
