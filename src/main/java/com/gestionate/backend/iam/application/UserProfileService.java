package com.gestionate.backend.iam.application;

import com.gestionate.backend.iam.domain.model.Citizen;
import com.gestionate.backend.iam.domain.model.CleaningOperationsStaff;
import com.gestionate.backend.iam.domain.model.MunicipalReceptionist;
import com.gestionate.backend.iam.domain.model.User;
import com.gestionate.backend.iam.domain.model.UserRole;
import com.gestionate.backend.iam.domain.repository.CitizenRepository;
import com.gestionate.backend.iam.domain.repository.CleaningOperationsStaffRepository;
import com.gestionate.backend.iam.domain.repository.MunicipalReceptionistRepository;
import com.gestionate.backend.iam.domain.repository.UserRepository;
import com.gestionate.backend.iam.infrastructure.mapping.UserProfileMapper;
import com.gestionate.backend.iam.interfaces.rest.dto.UpdateProfileRequest;
import com.gestionate.backend.iam.interfaces.rest.dto.UserProfileResponse;
import com.gestionate.backend.shared.application.DistrictService;
import com.gestionate.backend.shared.application.MunicipalityService;
import com.gestionate.backend.shared.application.util.TextNormalizer;
import com.gestionate.backend.shared.domain.model.District;
import com.gestionate.backend.shared.domain.model.Municipality;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserProfileService implements IUserProfileService {

    private final UserRepository userRepository;
    private final CitizenRepository citizenRepository;
    private final MunicipalReceptionistRepository municipalReceptionistRepository;
    private final CleaningOperationsStaffRepository cleaningOperationsStaffRepository;
    private final DistrictService districtService;
    private final MunicipalityService municipalityService;
    private final UserProfileMapper userProfileMapper;

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getMyProfile(String email) {
        User user = findUserByEmail(email);

        if (UserRole.CITIZEN.equals(user.getRole())) {
            Citizen citizen = citizenRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "El perfil del usuario no está configurado correctamente."));

            return userProfileMapper.toResponse(user, citizen);
        }

        if (UserRole.MUNICIPAL_RECEPTIONIST.equals(user.getRole())) {
            MunicipalReceptionist municipalReceptionist = municipalReceptionistRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "El perfil del usuario no está configurado correctamente."));

            return userProfileMapper.toResponse(user, municipalReceptionist);
        }

        if (UserRole.CLEANING_OPERATIONS.equals(user.getRole())) {
            CleaningOperationsStaff cleaningOperationsStaff = cleaningOperationsStaffRepository
                    .findByUser_Id(user.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "El perfil del usuario no está configurado correctamente."));

            return userProfileMapper.toResponse(user, cleaningOperationsStaff);
        }

        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "El rol del usuario no está configurado correctamente.");
    }

    @Override
    @Transactional
    public UserProfileResponse updateMyProfile(
            String email,
            UpdateProfileRequest request) {
        User user = findUserByEmail(email);

        validateRequiredFields(request);

        user.setFirstName(TextNormalizer.normalizeText(request.firstName()));
        user.setLastName(TextNormalizer.normalizeText(request.lastName()));
        user.setPhone(TextNormalizer.normalizeText(request.phone()));

        User savedUser = userRepository.save(user);

        if (UserRole.CITIZEN.equals(savedUser.getRole())) {
            Citizen citizen = citizenRepository.findByUser_Id(savedUser.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "El perfil del usuario no está configurado correctamente."));

            District district = districtService.resolveDistrict(
                    request.districtId(),
                    request.districtName(),
                    request.province());

            citizen.setDistrict(district);
            citizen.setHomeAddress(TextNormalizer.normalizeText(request.homeAddress()));

            Citizen savedCitizen = citizenRepository.save(citizen);

            return userProfileMapper.toResponse(savedUser, savedCitizen);
        }

        if (UserRole.MUNICIPAL_RECEPTIONIST.equals(savedUser.getRole())) {
            MunicipalReceptionist municipalReceptionist = municipalReceptionistRepository
                    .findByUser_Id(savedUser.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "El perfil del usuario no está configurado correctamente."));

            Municipality municipality = municipalityService.resolveMunicipality(
                    request.municipalityId(),
                    request.municipalityName(),
                    request.districtId(),
                    request.districtName(),
                    request.province());

            municipalReceptionist.setMunicipality(municipality);
            municipalReceptionist.setMunicipalUnit(request.municipalUnit());

            MunicipalReceptionist savedMunicipalReceptionist = municipalReceptionistRepository
                    .save(municipalReceptionist);

            return userProfileMapper.toResponse(savedUser, savedMunicipalReceptionist);
        }

        if (UserRole.CLEANING_OPERATIONS.equals(savedUser.getRole())) {
            CleaningOperationsStaff cleaningOperationsStaff = cleaningOperationsStaffRepository
                    .findByUser_Id(savedUser.getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "El perfil del usuario no está configurado correctamente."));

            Municipality municipality = municipalityService.resolveMunicipality(
                    request.municipalityId(),
                    request.municipalityName(),
                    request.districtId(),
                    request.districtName(),
                    request.province());

            cleaningOperationsStaff.setMunicipality(municipality);
            cleaningOperationsStaff.setShift(request.shift());

            CleaningOperationsStaff savedCleaningOperationsStaff = cleaningOperationsStaffRepository
                    .save(cleaningOperationsStaff);

            return userProfileMapper.toResponse(savedUser, savedCleaningOperationsStaff);
        }

        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "El rol del usuario no está configurado correctamente.");
    }

    private User findUserByEmail(String email) {
        String normalizedEmail = TextNormalizer.normalizeEmail(email);

        return userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado."));
    }

    private void validateRequiredFields(UpdateProfileRequest request) {
        if (!TextNormalizer.hasText(request.firstName())
                || !TextNormalizer.hasText(request.lastName())
                || !TextNormalizer.hasText(request.phone())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Completa los campos obligatorios.");
        }
    }
}
