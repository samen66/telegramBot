package com.github.samen66.javarushtelegrambot.command;

import org.junit.jupiter.api.DisplayName;

import static com.github.samen66.javarushtelegrambot.command.CommandName.STOP;
import static com.github.samen66.javarushtelegrambot.command.StopCommand.STOP_MESSAGE;

@DisplayName("Unit-level testing for Stop Command")
class StopCommandTest extends AbstractCommandTest{

    @Override
    String getCommandName() {
        return STOP.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return STOP_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new StopCommand(sendBotMessageService, telegramUserService);
    }
}