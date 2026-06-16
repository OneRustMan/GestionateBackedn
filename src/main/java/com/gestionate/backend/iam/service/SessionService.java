package com.gestionate.backend.iam.service;

import com.gestionate.backend.iam.repository.UserRepository;
import com.gestionate.backend.iam.dto.LogoutResponse;
import com.gestionate.backend.shared.util.TextNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SessionService implements ISessionService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public LogoutResponse logout(String email) {
        String normalizedEmail = TextNormalizer.normalizeEmail(email);

        if (!userRepository.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Sesión no válida.");
        }

        return new LogoutResponse("Sesión cerrada correctamente.");
    }
}
