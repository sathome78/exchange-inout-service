package com.exrates.inout.domain.main;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MerchantImage {
    private int Id;
    private int merchantId;
    private int currencyId;
    private String image_name;
    private String image_path;

    public MerchantImage(int id) {
        Id = id;
    }
}