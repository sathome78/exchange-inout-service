package com.exrates.inout.domain.main;

import com.exrates.inout.domain.enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRoleSettings {
    private UserRole userRole;
    private boolean isOrderAcceptionSameRoleOnly;
    private boolean isBotAcceptionAllowedOnly;
    private boolean isManualChangeAllowed;
    private boolean isConsideredForPriceRange;
}
