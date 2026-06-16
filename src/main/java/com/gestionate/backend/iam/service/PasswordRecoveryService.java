package com.gestionate.backend.iam.service;

import com.gestionate.backend.iam.model.PasswordResetCode;
import com.gestionate.backend.iam.model.User;
import com.gestionate.backend.iam.repository.PasswordResetCodeRepository;
import com.gestionate.backend.iam.repository.UserRepository;
import com.gestionate.backend.iam.dto.ForgotPasswordRequest;
import com.gestionate.backend.iam.dto.PasswordRecoveryResponse;
import com.gestionate.backend.iam.dto.ResetPasswordRequest;
import com.gestionate.backend.shared.util.TextNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryService implements IPasswordRecoveryService {

    private static final int CODE_EXPIRATION_MINUTES = 10;

    private final UserRepository userRepository;
    private final PasswordResetCodeRepository passwordResetCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    @Transactional
    public PasswordRecoveryResponse requestPasswordRecovery(ForgotPasswordRequest request) {

        String email = TextNormalizer.normalizeEmail(request.email());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "No se pudo recuperar la contraseña. Verifica la información ingresada."));

        List<PasswordResetCode> activeCodes = passwordResetCodeRepository.findByUser_IdAndUsedFalse(
                user.getId());

        activeCodes.forEach(code -> code.setUsed(true));
        passwordResetCodeRepository.saveAll(activeCodes);

        String recoveryCode = generateRecoveryCode();
        LocalDateTime now = LocalDateTime.now();

        PasswordResetCode passwordResetCode = PasswordResetCode.builder()
                .user(user)
                .code(recoveryCode)
                .expiresAt(now.plusMinutes(CODE_EXPIRATION_MINUTES))
                .used(false)
                .createdAt(now)
                .build();

        passwordResetCodeRepository.save(passwordResetCode);

        emailService.sendPasswordResetCode(
                user.getEmail(),
                recoveryCode);

        return new PasswordRecoveryResponse(
                "Código de recuperación enviado correctamente.");
    }

    @Override
    @Transactional
    public PasswordRecoveryResponse resetPassword(ResetPasswordRequest request) {

        String email = TextNormalizer.normalizeEmail(request.email());

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Las contraseñas no coinciden.");
        }

        PasswordResetCode passwordResetCode = passwordResetCodeRepository
                .findTopByUser_EmailAndCodeAndUsedFalseOrderByCreatedAtDesc(
                        email,
                        request.code())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "No se pudo recuperar la contraseña. Verifica la información ingresada."));

        if (passwordResetCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se pudo recuperar la contraseña. Verifica la información ingresada.");
        }

        User user = passwordResetCode.getUser();

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        passwordResetCode.setUsed(true);

        userRepository.save(user);
        passwordResetCodeRepository.save(passwordResetCode);

        return new PasswordRecoveryResponse(
                "Contraseña actualizada correctamente.");
    }

    private String generateRecoveryCode() {
        int number = secureRandom.nextInt(1_000_000);
        return String.format("%06d", number);
    }
}
