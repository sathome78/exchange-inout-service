package com.exrates.inout.dao;

import com.exrates.inout.domain.dto.MerchantSpecParamDto;

public interface MerchantSpecParamsDao {

    MerchantSpecParamDto getByMerchantNameAndParamName(String merchantName, String paramName);

    MerchantSpecParamDto getByMerchantIdAndParamName(int merchantId, String paramName);

    boolean updateParam(String merchantName, String paramName, String newValue);
}
