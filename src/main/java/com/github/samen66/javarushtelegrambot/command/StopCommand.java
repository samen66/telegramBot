package com.github.samen66.javarushtelegrambot.command;

import com.github.samen66.javarushtelegrambot.service.SendBotMessageService;
import com.github.samen66.javarushtelegrambot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Stop {@link Command}.
 */
public class StopCommand implements Command{

    private final SendBotMessageService sendBotMessageService;
    private final TelegramUserService telegramUserService;

    public static final String STOP_MESSAGE = "Деактивировал все ваши подписки \uD83D\uDE1F.";

    public StopCommand(SendBotMessageService sendBotMessageService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        String chatID = update.getMessage().getChatId().toString();
        sendBotMessageService.sendMessage(chatID, STOP_MESSAGE);
        telegramUserService.findByChatId(chatID).ifPresent(it ->{
            it.setActive(false);
            telegramUserService.save(it);
        });
    }

}
