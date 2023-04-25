package com.github.samen66.javarushtelegrambot.command;

import static com.github.samen66.javarushtelegrambot.command.CommandName.ADMIN_HELP;
import static org.junit.jupiter.api.Assertions.*;

class AdminHelpCommandTest extends AbstractCommandTest {

    @Override
    String getCommandName() {
        return ADMIN_HELP.getCommandName();
    }

    @Override
    String getCommandMessage() {
        return AdminHelpCommand.ADMIN_HELP_MESSAGE;
    }

    @Override
    Command getCommand() {
        return new AdminHelpCommand(sendBotMessageService);
    }
}