package com.exrates.inout.service.impl;

import com.exrates.inout.domain.enums.NotificatorSubscriptionStateEnum;
import com.exrates.inout.domain.main.TelegramSubscription;
import com.exrates.inout.exceptions.MessageUndeliweredException;
import com.exrates.inout.exceptions.TelegramSubscriptionException;
import com.exrates.inout.service.Subscribable;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.stream.Stream;


@PropertySource("classpath:telegram_bot.properties")
@Log4j2(topic = "message_notify")
@Component
public class TelegramBotService extends TelegramLongPollingBot {

    @Qualifier("telegramNotificatorServiceImpl")
    @Autowired
    private Subscribable subscribable;

    private @Value("${telegram.bot.key}")
    String key;
    private @Value("${telegram.bot.username}")
    String botName;

    static {
        ApiContextInitializer.init();
    }

    @PostConstruct
    private void init() {
        if (Stream.of("exrates_local_test_bot", "exrates_test_bot").noneMatch(p -> p.equalsIgnoreCase(botName))) {
            log.debug("init telegram bot {}", botName);
            TelegramBotsApi botsApi = new TelegramBotsApi();
            try {
                botsApi.registerBot(this);
            } catch (TelegramApiException e) {
                log.error("error while initialize bot {}", e);
            }
        }
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage()
                .setChatId(chatId)
                .setText(text);
        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            throw new MessageUndeliweredException();
        }
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String sender = update.getMessage().getFrom().getUserName();
            Long chatId = update.getMessage().getChat().getId();
            String text = update.getMessage().getText();
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId());
            if (text.startsWith("/")) {
                message.setText("Hello!");
            } else {
                try {
                    subscribable.subscribe(TelegramSubscription.builder()
                            .chatId(chatId)
                            .rawText(text)
                            .subscriptionState(NotificatorSubscriptionStateEnum.getFinalState())
                            .userAccount(sender)
                            .build());
                    message.setText("Registered");
                } catch (Exception e) {
                    log.error(e);
                    message.setText("error registering profile");
                }
            }
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                throw new TelegramSubscriptionException();
            }

        }
    }

    public String getBotUsername() {
        return botName;
    }

    public String getBotToken() {
        return key;
    }

}
