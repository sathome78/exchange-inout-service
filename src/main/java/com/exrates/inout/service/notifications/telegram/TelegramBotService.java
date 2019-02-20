package com.exrates.inout.service.notifications.telegram;

import lombok.extern.log4j.Log4j2;
import me.exrates.model.dto.TelegramSubscription;
import me.exrates.model.enums.NotificatorSubscriptionStateEnum;
import me.exrates.service.exception.MessageUndeliweredException;
import me.exrates.service.exception.TelegramSubscriptionException;
import me.exrates.service.notifications.Subscribable;
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


/**
 * Created by Maks on 05.10.2017.
 */
@PropertySource("classpath:telegram_bot.properties")
@Log4j2(topic = "message_notify")
@Component
public class TelegramBotService  extends TelegramLongPollingBot {

    @Qualifier("telegramNotificatorServiceImpl")
    @Autowired
    private Subscribable subscribable;

    private @Value("${telegram.bot.key}") String key;
    private @Value("${telegram.bot.username}") String botName;
    private @Value("${enable}") boolean enable;

    static {ApiContextInitializer.init();}


    @PostConstruct
    private void init() {
        if (!enable) return;
        if (Stream.of("exrates_local_test_bot", "exrates_test_bot").noneMatch(p->p.equalsIgnoreCase(botName))) {
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

    @Override
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

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return key;
    }


}
