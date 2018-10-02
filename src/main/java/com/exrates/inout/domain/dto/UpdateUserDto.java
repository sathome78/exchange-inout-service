package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.UserStatus;
import lombok.Data;


@Data
public class UpdateUserDto {
    private int id;
    private String email;
    private String phone;
    private UserStatus status;
    private String password;
    private String finpassword;
    private UserRole role;
}