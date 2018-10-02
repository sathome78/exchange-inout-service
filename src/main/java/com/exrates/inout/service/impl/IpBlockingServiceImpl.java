package com.exrates.inout.service.impl;

import com.exrates.inout.domain.dto.LoginAttemptDto;
import com.exrates.inout.domain.enums.IpTypesOfChecking;
import com.exrates.inout.exceptions.BannedIpException;
import com.exrates.inout.service.IpBlockingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentMap;

import static com.exrates.inout.domain.enums.IpBanStatus.*;

@Log4j2(topic = "ip_log")
@Service
public class IpBlockingServiceImpl implements IpBlockingService {

    @Value("${ban.short.attempts.num}")
    private Integer attemptsBeforeShortBan;

    @Value("${ban.long.attempts.num}")
    private Integer attemptsBeforeLongBan;

    @Value("${ban.short.attempts.period}")
    private Long periodAttemptsBeforeShortBan;

    @Value("${ban.long.attempts.period}")
    private Long periodAttemptsBeforeLongBan;

    @Value("${ban.short.time}")
    private Long shortBanTime;

    @Value("${ban.long.time}")
    private Long longBanTime;

    private final Object lock = new Object();

    private ConcurrentMap<IpTypesOfChecking, ConcurrentMap<String, LoginAttemptDto>> ipchecker;

    public IpBlockingServiceImpl() {
        ipchecker = new ConcurrentReferenceHashMap<>();
        ipchecker.put(IpTypesOfChecking.LOGIN, new ConcurrentReferenceHashMap<>());
        ipchecker.put(IpTypesOfChecking.OPEN_API, new ConcurrentReferenceHashMap<>());
    }

    public void checkIp(String ipAddress, IpTypesOfChecking ipTypesOfChecking) {
        synchronized (lock) {
            ConcurrentMap<String, LoginAttemptDto> specificIpChecker = ipchecker.get(ipTypesOfChecking);
            if (specificIpChecker.containsKey(ipAddress)) {
                LocalDateTime currentTime = LocalDateTime.now();
                LoginAttemptDto attempt = specificIpChecker.get(ipAddress);
                if ((attempt.getStatus() == BAN_SHORT && checkBanPending(attempt, shortBanTime, currentTime))) {
                    throw new BannedIpException("IP banned: number of incorrect attempts exceeded!", shortBanTime);
                } else if (attempt.getStatus() == BAN_LONG && checkBanPending(attempt, longBanTime, currentTime)) {
                    throw new BannedIpException("IP banned: number of incorrect attempts exceeded!", longBanTime);
                }
                if (attempt.getStatus() == BAN_LONG) {
                    specificIpChecker.remove(ipAddress);
                } else {
                    attempt.setStatus(ALLOW);
                }

            }
        }


    }

    private boolean checkBanPending(LoginAttemptDto attempt, long banTimeSeconds, LocalDateTime currentTime) {
        return currentTime.isBefore(attempt.getLastAttemptTime().plusSeconds(banTimeSeconds));
    }

}
