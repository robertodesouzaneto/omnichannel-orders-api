package com.example.omnichannel_orders_api.application.service;

import com.example.omnichannel_orders_api.api.exception.BusinessException;
import com.example.omnichannel_orders_api.api.exception.ConsentRequiredException;
import com.example.omnichannel_orders_api.application.dto.ConsentResponse;
import com.example.omnichannel_orders_api.domain.enums.ConsentAction;
import com.example.omnichannel_orders_api.domain.model.Consent;
import com.example.omnichannel_orders_api.domain.model.User;
import com.example.omnichannel_orders_api.infrastructure.repository.ConsentRepository;
import com.example.omnichannel_orders_api.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ConsentService {

    private final ConsentRepository consentRepository;
    private final UserRepository userRepository;

    @Transactional
    public ConsentResponse accept() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        ConsentAction latest = consentRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId())
                .map(Consent::getAction)
                .orElse(null);

        if (latest == ConsentAction.GRANTED) {
            throw new BusinessException("Consent is already active");
        }

        Consent consent = Consent.builder()
                .user(user)
                .action(ConsentAction.GRANTED)
                .build();

        return ConsentResponse.from(consentRepository.save(consent));
    }

    @Transactional
    public void grant(User user) {
        Consent consent = Consent.builder()
                .user(user)
                .action(ConsentAction.GRANTED)
                .build();
        consentRepository.save(consent);
    }

    @Transactional
    public ConsentResponse revoke() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        ConsentAction latest = consentRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId())
                .map(Consent::getAction)
                .orElse(null);

        if (latest == ConsentAction.REVOKED) {
            throw new BusinessException("Consent is already revoked");
        }

        Consent consent = Consent.builder()
                .user(user)
                .action(ConsentAction.REVOKED)
                .build();

        return ConsentResponse.from(consentRepository.save(consent));
    }

    public void verifyActiveConsent(User user) {
        ConsentAction latest = consentRepository.findTopByUserIdOrderByCreatedAtDesc(user.getId())
                .map(Consent::getAction)
                .orElse(null);

        if (latest != ConsentAction.GRANTED) {
            throw new ConsentRequiredException();
        }
    }

    public List<ConsentResponse> getHistory() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        return consentRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(ConsentResponse::from)
                .toList();
    }
}
