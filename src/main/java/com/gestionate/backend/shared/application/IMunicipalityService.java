package com.gestionate.backend.shared.application;

import com.gestionate.backend.shared.domain.model.Municipality;
import com.gestionate.backend.shared.domain.model.District;
import com.gestionate.backend.shared.interfaces.rest.dto.MunicipalityResponse;
import java.util.List;

public interface IMunicipalityService {

    Municipality resolveMunicipality(
            Long municipalityId,
            String municipalityName,
            Long districtId,
            String districtName,
            String province);

    Municipality createMunicipality(String name, District district);

    List<MunicipalityResponse> findActiveMunicipalities();
}
