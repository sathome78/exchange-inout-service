package com.exrates.inout.domain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

@Data
//@Log4j2(topic = "bitcoin_core")
public class CoreWalletDto {

   private static final Logger log = LogManager.getLogger("bitcoin_core");

    private int id;
    private int merchantId;
    private int currencyId;
    private String merchantName;
    private String currencyName;
    private String currencyDescription;
    private String titleCode;
    private String localizedTitle = "";

    @JsonIgnore
    private String TITLE_CODE_UNIVERSAL = "admin.btcWallet.title";


    public void localizeTitle(MessageSource messageSource, Locale locale) {
        try {
            this.localizedTitle = messageSource.getMessage(TITLE_CODE_UNIVERSAL, new Object[]{currencyDescription}, locale);
        } catch (NoSuchMessageException e) {
            log.error(e);
        }
    }
}
