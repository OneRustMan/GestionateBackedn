package com.gestionate.backend.iam.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.gestionate.backend.iam.model.Citizen;
import com.gestionate.backend.iam.model.CleaningOperationsStaff;
import com.gestionate.backend.iam.model.MunicipalReceptionist;
import com.gestionate.backend.iam.model.User;
import com.gestionate.backend.iam.model.UserRole;
import com.gestionate.backend.iam.repository.CitizenRepository;
import com.gestionate.backend.iam.repository.CleaningOperationsStaffRepository;
import com.gestionate.backend.iam.repository.MunicipalReceptionistRepository;
import com.gestionate.backend.iam.repository.UserRepository;
import com.gestionate.backend.iam.mapper.UserRegistrationMapper;
import com.gestionate.backend.iam.dto.RegisterCitizenRequest;
import com.gestionate.backend.iam.dto.RegisterCleaningOperationsStaffRequest;
import com.gestionate.backend.iam.dto.RegisterMunicipalReceptionistRequest;
import com.gestionate.backend.iam.dto.RegisterResponse;
import com.gestionate.backend.shared.service.DistrictService;
import com.gestionate.backend.shared.service.MunicipalityService;
import com.gestionate.backend.shared.util.TextNormalizer;
import com.gestionate.backend.shared.model.District;
import com.gestionate.backend.shared.model.Municipality;
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
    private final CleaningOperationsStaffRepository cleaningOperationsStaffRepository;
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
        validateUniqueReceptionistWorkerCode(workerCode);

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

    @Override
    @Transactional
    public RegisterResponse registerCleaningOperationsStaff(RegisterCleaningOperationsStaffRequest request) {

        validatePasswordsMatch(request.password(), request.confirmPassword());

        String email = TextNormalizer.normalizeEmail(request.email());
        String dni = TextNormalizer.normalizeText(request.dni());
        String phone = TextNormalizer.normalizeText(request.phone());
        String workerCode = TextNormalizer.normalizeUpperText(request.workerCode());

        validateUniqueUserData(email, dni);
        validateUniqueCleaningStaffWorkerCode(workerCode);

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
                .role(UserRole.CLEANING_OPERATIONS)
                .build();

        User savedUser = userRepository.save(user);

        CleaningOperationsStaff cleaningOperationsStaff = CleaningOperationsStaff.builder()
                .user(savedUser)
                .municipality(municipality)
                .workerCode(workerCode)
                .shift(request.shift())
                .build();

        CleaningOperationsStaff savedCleaningOperationsStaff = cleaningOperationsStaffRepository
                .save(cleaningOperationsStaff);

        return userRegistrationMapper.toRegisterResponse(savedUser, savedCleaningOperationsStaff);
    }

    private void validatePasswordsMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Las contraseñas no coinciden.");
        }
    }

    private void validateUniqueUserData(String email, String dni) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este correo ya está registrado.");
        }

        if (userRepository.existsByDni(dni)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El DNI ya está registrado.");
        }
    }

    private void validateUniqueReceptionistWorkerCode(String workerCode) {
        if (municipalReceptionistRepository.existsByWorkerCode(workerCode)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El código de trabajador ya está registrado.");
        }
    }

    private void validateUniqueCleaningStaffWorkerCode(String workerCode) {
        if (cleaningOperationsStaffRepository.existsByWorkerCode(workerCode)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El código de trabajador ya está registrado.");
        }
    }
}
