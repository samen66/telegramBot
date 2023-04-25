package com.github.samen66.javarushtelegrambot.command;

import com.github.samen66.javarushtelegrambot.javarushclient.JavaRushGroupClient;
import com.github.samen66.javarushtelegrambot.javarushclient.JavaRushGroupClientImpl;
import com.github.samen66.javarushtelegrambot.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;

@DisplayName("Unit-level testing for CommandContainer")
class CommandContainerTest {
    private CommandContainer commandContainer;
    @Value("#{'${bot.admins}'.split(',')}")
    private List<String> adminUSerNames;

    @BeforeEach
    public void init() {
        SendBotMessageService sendBotMessageService = Mockito.mock(SendBotMessageService.class);
        TelegramUserService telegramUserService = Mockito.mock(TelegramUserServiceImpl.class);
        JavaRushGroupClient javaRushGroupClient = Mockito.mock(JavaRushGroupClientImpl.class);
        GroupSubService groupSubService = Mockito.mock(GroupSubServiceImpl.class);
        StatisticsService statisticsService = Mockito.mock(StatisticsService.class);

        commandContainer = new CommandContainer(sendBotMessageService, telegramUserService, javaRushGroupClient, groupSubService, adminUSerNames, statisticsService);
    }

    @Test
    public void shouldGetAllTheExistingCommands() {
        //when-then
        Arrays.stream(CommandName.values())
                .forEach(commandName -> {
                    Command command = commandContainer.retrieveCommand(commandName.getCommandName(), "just");
                    Assertions.assertNotEquals(UnknownCommand.class, command.getClass());
                });
    }

    @Test
    public void shouldReturnUnknownCommand() {
        //given
        String unknownCommand = "/fgjhdfgdfg";

        //when
        Command command = commandContainer.retrieveCommand(unknownCommand, "just");

        //then
        Assertions.assertEquals(UnknownCommand.class, command.getClass());
    }

}