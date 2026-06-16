package com.gestionate.backend.shared.service;

import com.gestionate.backend.shared.dto.DistrictResponse;
import java.util.List;
import com.gestionate.backend.shared.model.District;

public interface IDistrictService {

    District resolveDistrict(Long districtId, String districtName, String province);

    District createDistrict(String name, String province);

    List<DistrictResponse> findActiveDistricts();
}
