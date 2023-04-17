package com.github.samen66.javarushtelegrambot.command;

import org.junit.jupiter.api.DisplayName;

import static com.github.samen66.javarushtelegrambot.command.CommandName.NO;
import static com.github.samen66.javarushtelegrambot.command.NoCommand.NO_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit-level testing for NoCommand")
public class NoCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return NO.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return NO_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new NoCommand(sendBotMessageService);
    }
}