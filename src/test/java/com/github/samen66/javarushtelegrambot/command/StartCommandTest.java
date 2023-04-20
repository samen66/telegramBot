package com.github.samen66.javarushtelegrambot.command;

import org.junit.jupiter.api.DisplayName;

import static com.github.samen66.javarushtelegrambot.command.CommandName.START;
import static com.github.samen66.javarushtelegrambot.command.StartCommand.START_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit-level testing for StartCommand")
class StartCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return START.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return START_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new StartCommand(sendBotMessageService, telegramUserService);
    }
}