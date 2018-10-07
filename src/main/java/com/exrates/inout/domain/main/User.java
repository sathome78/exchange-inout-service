package com.exrates.inout.domain.main;

import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.UserStatus;
import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
public class User {

    private int id;
    private String nickname;
    private String email;
    private String phone;
    private UserStatus status = UserStatus.REGISTERED;
    private UserStatus userStatus = UserStatus.REGISTERED;
    private String password;
    private String finpassword;
    private Date regdate;
    private String ipaddress;
    private String confirmPassword;
    private String confirmFinPassword;
    private boolean readRules;
    private UserRole role = UserRole.USER;
    private String parentEmail;
    private List<UserFile> userFiles = Collections.emptyList();
}