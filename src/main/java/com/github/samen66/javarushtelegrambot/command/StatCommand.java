package com.github.samen66.javarushtelegrambot.command;

import com.github.samen66.javarushtelegrambot.service.SendBotMessageService;
import com.github.samen66.javarushtelegrambot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StatCommand implements Command{
    private final TelegramUserService telegramUserServicel;
    private final SendBotMessageService sendBotMessageService;

    public final static String STAT_MESSAGE = "Javarush Telegram Bot использует %s человек.";

    @Autowired
    public StatCommand(TelegramUserService telegramUserServicel, SendBotMessageService sendBotMessageService) {
        this.telegramUserServicel = telegramUserServicel;
        this.sendBotMessageService = sendBotMessageService;
    }


    @Override
    public void execute(Update update) {
        int activeTelegramUsers = telegramUserServicel.retrieveAllActiveUsers().size();
        String chatID = update.getMessage().getChatId().toString();
        sendBotMessageService.sendMessage(chatID, String.format(STAT_MESSAGE, activeTelegramUsers));
    }
}
