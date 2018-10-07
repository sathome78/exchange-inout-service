package com.exrates.inout.domain.main;

import com.exrates.inout.domain.enums.UserRole;
import lombok.Data;

@Data
public class UserRoleSettings {
    private UserRole userRole;
    private boolean isOrderAcceptionSameRoleOnly;
    private boolean isBotAcceptionAllowedOnly;
    private boolean isManualChangeAllowed;
    private boolean isConsideredForPriceRange;
}
