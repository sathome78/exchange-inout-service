package com.exrates.inout.service;

import com.exrates.inout.domain.main.Email;

public interface SendMailService {

    void sendMail(Email email);

    void sendMailMandrill(Email email);

    void sendInfoMail(Email email);
}
