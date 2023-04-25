package com.github.samen66.javarushtelegrambot.command;

import com.github.samen66.javarushtelegrambot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.samen66.javarushtelegrambot.command.CommandName.STAT;

public class AdminHelpCommand implements Command{
    private final SendBotMessageService sendBotMessageService;

    public static final String ADMIN_HELP_MESSAGE = String.format("✨<b>Доступные команды админа</b>✨\n\n"
                    + "<b>Получить статистику</b>\n"
                    + "%s - статистика бота\n",
            STAT.getCommandName());

    public AdminHelpCommand(SendBotMessageService sendBotMessageService) {
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        sendBotMessageService.sendMessage(update.getMessage().getChatId().toString(), ADMIN_HELP_MESSAGE);
    }
}
