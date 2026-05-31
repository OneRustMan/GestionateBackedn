package com.gestionate.backend.shared.application;

import com.gestionate.backend.shared.domain.model.District;

public interface IDistrictService {

    District resolveDistrict(Long districtId, String districtName, String province);

    District createDistrict(String name, String province);
}
