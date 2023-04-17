package com.github.samen66.javarushtelegrambot.bot;

import com.github.samen66.javarushtelegrambot.command.CommandContainer;
import com.github.samen66.javarushtelegrambot.service.SendBotMessageServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static com.github.samen66.javarushtelegrambot.command.CommandName.NO;

@Component
public class JavaRushTelegramBot extends TelegramLongPollingBot {

    public static String COMMAND_PREFIX = "/";
    @Value("${bot.username}")
    private String username;
    @Value("${bot.token}")
    private String token;

    private final CommandContainer commandContainer;

    public JavaRushTelegramBot() {
        this.commandContainer = new CommandContainer(new SendBotMessageServiceImpl(this));
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String message = update.getMessage().getText().trim();
            if (message.startsWith(COMMAND_PREFIX)){
                String commandIdentifier = message.split(" ")[0].toLowerCase();
                commandContainer.retrieveCommand(commandIdentifier).execute(update);
            }else{
                commandContainer.retrieveCommand(NO.getCommandName()).execute(update);
            }
        }

    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
