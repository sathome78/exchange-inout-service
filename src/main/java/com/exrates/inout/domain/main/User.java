package com.exrates.inout.domain.main;

import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class User implements Serializable {

    private int id;
    private String nickname;
    private String email;
    private String phone;
    @JsonProperty("status")
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
    private String kycStatus;
    private String country;
    private String firstName;
    private String lastName;
    private Date birthDay;
    private String publicId;

    public User() {
    }

    public User(int userId) {
        this.id = userId;
    }

    public User(int id, String email) {
        this(id);
        this.email = email;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isReadRules() {
        return readRules;
    }

    public void setReadRules(boolean readRules) {
        this.readRules = readRules;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getIp() {
        return ipaddress;
    }

    public void setIp(String ip) {
        this.ipaddress = ip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFinpassword() {
        return finpassword;
    }

    public void setFinpassword(String finpassword) {
        this.finpassword = finpassword;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public String getConfirmFinPassword() {
        return confirmFinPassword;
    }

    public void setConfirmFinPassword(String confirmFinPassword) {
        this.confirmFinPassword = confirmFinPassword;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(final String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", userStatus=" + userStatus +
                ", regdate=" + regdate +
                ", ipaddress='" + ipaddress + '\'' +
                ", readRules=" + readRules +
                ", role=" + role +
                ", parentEmail='" + parentEmail + '\'' +
                ", userFiles=" + userFiles +
                '}';
    }
}