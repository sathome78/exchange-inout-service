package com.exrates.inout.service;


import com.exrates.inout.domain.enums.IpTypesOfChecking;

public interface IpBlockingService {

    void checkIp(String ip, IpTypesOfChecking typeOfChecking);

}
