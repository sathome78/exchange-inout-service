package com.exrates.inout.dto;

import com.exrates.inout.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestUser {
    private Integer id;
    private String email;
    private UserRole role = UserRole.USER;

    public TestUser(Integer id, String email) {
        this.id = id;
        this.email = email;
    }
}
