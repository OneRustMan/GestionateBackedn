package com.gestionate.backend.iam.application;

import com.gestionate.backend.iam.domain.model.User;
import com.gestionate.backend.iam.domain.model.UserRole;
import com.gestionate.backend.iam.domain.repository.CitizenRepository;
import com.gestionate.backend.iam.domain.repository.CleaningOperationsStaffRepository;
import com.gestionate.backend.iam.domain.repository.MunicipalReceptionistRepository;
import com.gestionate.backend.iam.domain.repository.UserRepository;
import com.gestionate.backend.iam.infrastructure.mapping.LoginMapper;
import com.gestionate.backend.iam.interfaces.rest.dto.LoginRequest;
import com.gestionate.backend.iam.interfaces.rest.dto.LoginResponse;
import com.gestionate.backend.shared.application.util.TextNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final UserRepository userRepository;
    private final CitizenRepository citizenRepository;
    private final MunicipalReceptionistRepository municipalReceptionistRepository;
    private final CleaningOperationsStaffRepository cleaningOperationsStaffRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final LoginMapper loginMapper;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        String email = TextNormalizer.normalizeEmail(request.email());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuario o contraseña incorrectos."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario o contraseña incorrectos.");
        }

        Long profileId = resolveProfileId(user);

        String token = jwtService.generateToken(user);

        return loginMapper.toResponse(
                user,
                profileId,
                token);
    }

    private Long resolveProfileId(User user) {
        if (UserRole.CITIZEN.equals(user.getRole())) {
            return citizenRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "El perfil del usuario no está configurado correctamente."))
                    .getId();
        }

        if (UserRole.MUNICIPAL_RECEPTIONIST.equals(user.getRole())) {
            return municipalReceptionistRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "El perfil del usuario no está configurado correctamente."))
                    .getId();
        }

        if (UserRole.CLEANING_OPERATIONS.equals(user.getRole())) {
            return cleaningOperationsStaffRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "El perfil del usuario no está configurado correctamente."))
                    .getId();
        }

        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "El rol del usuario no está configurado correctamente.");
    }
}
