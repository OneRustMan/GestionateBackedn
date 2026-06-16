package com.gestionate.backend.shared.service;

import com.gestionate.backend.shared.model.Municipality;
import com.gestionate.backend.shared.model.District;
import com.gestionate.backend.shared.dto.MunicipalityResponse;
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
