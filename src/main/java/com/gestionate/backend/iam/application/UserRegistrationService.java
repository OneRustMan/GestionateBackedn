package com.gestionate.backend.iam.application;

import com.gestionate.backend.iam.domain.model.Citizen;
import com.gestionate.backend.iam.domain.model.MunicipalReceptionist;
import com.gestionate.backend.iam.domain.model.User;
import com.gestionate.backend.iam.domain.model.UserRole;
import com.gestionate.backend.iam.domain.repository.CitizenRepository;
import com.gestionate.backend.iam.domain.repository.MunicipalReceptionistRepository;
import com.gestionate.backend.iam.domain.repository.UserRepository;
import com.gestionate.backend.iam.infrastructure.mapping.UserRegistrationMapper;
import com.gestionate.backend.iam.interfaces.rest.dto.RegisterCitizenRequest;
import com.gestionate.backend.iam.interfaces.rest.dto.RegisterMunicipalReceptionistRequest;
import com.gestionate.backend.iam.interfaces.rest.dto.RegisterResponse;
import com.gestionate.backend.shared.application.DistrictService;
import com.gestionate.backend.shared.application.MunicipalityService;
import com.gestionate.backend.shared.application.util.TextNormalizer;
import com.gestionate.backend.shared.domain.model.District;
import com.gestionate.backend.shared.domain.model.Municipality;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService implements IUserRegistrationService {

    private final UserRepository userRepository;
    private final CitizenRepository citizenRepository;
    private final MunicipalReceptionistRepository municipalReceptionistRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRegistrationMapper userRegistrationMapper;
    private final DistrictService districtService;
    private final MunicipalityService municipalityService;

    @Override
    @Transactional
    public RegisterResponse registerCitizen(RegisterCitizenRequest request) {

        validatePasswordsMatch(request.password(), request.confirmPassword());

        String email = TextNormalizer.normalizeEmail(request.email());
        String dni = TextNormalizer.normalizeText(request.dni());
        String phone = TextNormalizer.normalizeText(request.phone());

        validateUniqueUserData(email, dni);

        District district = districtService.resolveDistrict(
                request.districtId(),
                request.districtName(),
                request.province());

        User user = User.builder()
                .firstName(TextNormalizer.normalizeText(request.firstName()))
                .lastName(TextNormalizer.normalizeText(request.lastName()))
                .dni(dni)
                .phone(phone)
                .email(email)
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(UserRole.CITIZEN)
                .build();

        User savedUser = userRepository.save(user);

        Citizen citizen = Citizen.builder()
                .user(savedUser)
                .district(district)
                .homeAddress(TextNormalizer.normalizeText(request.homeAddress()))
                .build();

        Citizen savedCitizen = citizenRepository.save(citizen);

        return userRegistrationMapper.toRegisterResponse(savedUser, savedCitizen);
    }

    @Override
    @Transactional
    public RegisterResponse registerMunicipalReceptionist(RegisterMunicipalReceptionistRequest request) {

        validatePasswordsMatch(request.password(), request.confirmPassword());

        String email = TextNormalizer.normalizeEmail(request.email());
        String dni = TextNormalizer.normalizeText(request.dni());
        String phone = TextNormalizer.normalizeText(request.phone());
        String workerCode = TextNormalizer.normalizeUpperText(request.workerCode());

        validateUniqueUserData(email, dni);
        validateUniqueWorkerCode(workerCode);

        Municipality municipality = municipalityService.resolveMunicipality(
                request.municipalityId(),
                request.municipalityName(),
                request.districtId(),
                request.districtName(),
                request.province());

        User user = User.builder()
                .firstName(TextNormalizer.normalizeText(request.firstName()))
                .lastName(TextNormalizer.normalizeText(request.lastName()))
                .dni(dni)
                .phone(phone)
                .email(email)
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(UserRole.MUNICIPAL_RECEPTIONIST)
                .build();

        User savedUser = userRepository.save(user);

        MunicipalReceptionist municipalReceptionist = MunicipalReceptionist.builder()
                .user(savedUser)
                .municipality(municipality)
                .municipalUnit(request.municipalUnit())
                .workerCode(workerCode)
                .build();

        MunicipalReceptionist savedMunicipalReceptionist = municipalReceptionistRepository.save(municipalReceptionist);

        return userRegistrationMapper.toRegisterResponse(savedUser, savedMunicipalReceptionist);
    }

    private void validatePasswordsMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }
    }

    private void validateUniqueUserData(String email, String dni) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        if (userRepository.existsByDni(dni)) {
            throw new IllegalArgumentException("El DNI ya está registrado.");
        }
    }

    private void validateUniqueWorkerCode(String workerCode) {
        if (municipalReceptionistRepository.existsByWorkerCode(workerCode)) {
            throw new IllegalArgumentException("El código de trabajador ya está registrado.");
        }
    }
}
