package com.github.samen66.javarushtelegrambot.command;

import static com.github.samen66.javarushtelegrambot.command.CommandName.STAT;
import static org.junit.jupiter.api.Assertions.*;

class StatCommandTest extends AbstractCommandTest{

    @Override
    String getCommandName() {
        return STAT.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return String.format(StatCommand.STAT_MESSAGE, 0);
    }

    @Override
    Command getCommand() {
        return new StatCommand(telegramUserService, sendBotMessageService);
    }
}