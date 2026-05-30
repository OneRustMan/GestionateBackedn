package com.gestionate.backend.iam.application;

import com.gestionate.backend.iam.domain.model.Citizen;
import com.gestionate.backend.iam.domain.model.User;
import com.gestionate.backend.iam.domain.model.UserRole;
import com.gestionate.backend.iam.domain.repository.CitizenRepository;
import com.gestionate.backend.iam.domain.repository.UserRepository;
import com.gestionate.backend.iam.infrastructure.mapping.UserRegistrationMapper;
import com.gestionate.backend.iam.interfaces.rest.dto.RegisterCitizenRequest;
import com.gestionate.backend.iam.interfaces.rest.dto.RegisterResponse;
import com.gestionate.backend.shared.domain.model.District;
import com.gestionate.backend.shared.domain.repository.DistrictRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final CitizenRepository citizenRepository;
    private final DistrictRepository districtRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRegistrationMapper userRegistrationMapper;

    @Transactional
    public RegisterResponse registerCitizen(RegisterCitizenRequest request) {

        validatePasswordsMatch(request.password(), request.confirmPassword());

        String email = normalizeEmail(request.email());
        String dni = request.dni().trim();
        String phone = request.phone().trim();

        validateUniqueUserData(email, dni);

        District district = resolveDistrict(request);

        User user = User.builder()
                .firstName(normalizeText(request.firstName()))
                .lastName(normalizeText(request.lastName()))
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
                .homeAddress(normalizeText(request.homeAddress()))
                .build();

        Citizen savedCitizen = citizenRepository.save(citizen);

        return userRegistrationMapper.toRegisterResponse(
                savedUser,
                savedCitizen,
                "Ciudadano registrado correctamente.");
    }

    private District resolveDistrict(RegisterCitizenRequest request) {

        boolean hasDistrictId = request.districtId() != null;
        boolean hasDistrictText = hasText(request.districtName()) || hasText(request.province());

        if (hasDistrictId && hasDistrictText) {
            throw new IllegalArgumentException(
                    "Debe enviar districtId o districtName/province, pero no ambos.");
        }

        if (hasDistrictId) {
            District district = districtRepository.findById(request.districtId())
                    .orElseThrow(() -> new IllegalArgumentException("El distrito seleccionado no existe."));

            if (!Boolean.TRUE.equals(district.getActive())) {
                throw new IllegalArgumentException("El distrito seleccionado no está activo.");
            }

            return district;
        }

        if (!hasText(request.districtName()) || !hasText(request.province())) {
            throw new IllegalArgumentException(
                    "Debe enviar un distrito existente mediante districtId o escribir districtName y province.");
        }

        String normalizedDistrictName = normalizeUpperText(request.districtName());
        String normalizedProvince = normalizeUpperText(request.province());

        return districtRepository
                .findByNameAndProvince(normalizedDistrictName, normalizedProvince)
                .orElseGet(() -> createDistrict(normalizedDistrictName, normalizedProvince));
    }

    private District createDistrict(String name, String province) {
        District district = District.builder()
                .name(name)
                .province(province)
                .active(true)
                .build();

        return districtRepository.save(district);
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

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeText(String value) {
        return value.trim().replaceAll("\\s+", " ");
    }

    private String normalizeUpperText(String value) {
        return normalizeText(value).toUpperCase(Locale.ROOT);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
