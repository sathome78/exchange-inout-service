package com.exrates.inout.service.achain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchainContract {

    private String contract;
    private String curencyName;
    private String merchantName;
    private String name;
}
