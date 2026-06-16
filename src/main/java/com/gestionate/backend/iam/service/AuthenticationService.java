package com.gestionate.backend.iam.service;

import com.gestionate.backend.iam.model.User;
import com.gestionate.backend.iam.model.UserRole;
import com.gestionate.backend.iam.repository.CitizenRepository;
import com.gestionate.backend.iam.repository.CleaningOperationsStaffRepository;
import com.gestionate.backend.iam.repository.MunicipalReceptionistRepository;
import com.gestionate.backend.iam.repository.UserRepository;
import com.gestionate.backend.iam.mapper.LoginMapper;
import com.gestionate.backend.iam.dto.LoginRequest;
import com.gestionate.backend.iam.dto.LoginResponse;
import com.gestionate.backend.shared.util.TextNormalizer;
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
