package com.github.samen66.javarushtelegrambot.command;

import com.github.samen66.javarushtelegrambot.entity.TelegramUser;
import com.github.samen66.javarushtelegrambot.service.SendBotMessageService;
import com.github.samen66.javarushtelegrambot.service.TelegramUserService;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;
import java.util.stream.Collectors;

public class ListGroupSubCommand implements Command{

    private final TelegramUserService telegramUserService;
    private final SendBotMessageService sendBotMessageService;

    public ListGroupSubCommand(TelegramUserService telegramUserService, SendBotMessageService sendBotMessageService) {
        this.telegramUserService = telegramUserService;
        this.sendBotMessageService = sendBotMessageService;
    }

    @Override
    public void execute(Update update) {
        //TODO
        //get user
        //get users groupSub
        //send user message
        String chatId = update.getMessage().getChatId().toString();
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
        //TODO exception
        String message = "List of group that you followed \n\n";
        String messageListUserGroupSub = telegramUser.getGroupSubs().stream()
                .map(groupSub -> String.format("%s - %s\n", groupSub.getId(), groupSub.getTitle()))
                .collect(Collectors.joining());
        sendBotMessageService.sendMessage(chatId, message + messageListUserGroupSub);
    }
}
