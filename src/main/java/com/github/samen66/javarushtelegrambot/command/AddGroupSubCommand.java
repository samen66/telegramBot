package com.github.samen66.javarushtelegrambot.command;

import com.github.samen66.javarushtelegrambot.entity.GroupSub;
import com.github.samen66.javarushtelegrambot.javarushclient.GroupRequestArgs;
import com.github.samen66.javarushtelegrambot.javarushclient.JavaRushGroupClient;
import com.github.samen66.javarushtelegrambot.javarushclient.dto.GroupDiscussionInfo;
import com.github.samen66.javarushtelegrambot.service.GroupSubService;
import com.github.samen66.javarushtelegrambot.service.SendBotMessageService;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.Collectors;

import static com.github.samen66.javarushtelegrambot.command.CommandName.ADD_GROUP_SUB;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public class AddGroupSubCommand implements Command{
    private final SendBotMessageService sendBotMessageService;
    private final JavaRushGroupClient javaRushGroupClient;
    private final GroupSubService groupSubService;

    public AddGroupSubCommand(SendBotMessageService sendBotMessageService, JavaRushGroupClient javaRushGroupClient, GroupSubService groupSubService) {
        this.sendBotMessageService = sendBotMessageService;
        this.javaRushGroupClient = javaRushGroupClient;
        this.groupSubService = groupSubService;
    }


    @Override
    public void execute(Update update) {
        if (update.hasMessage()){
            if(update.getMessage().getText().equalsIgnoreCase(ADD_GROUP_SUB.getCommandName())){
                sendGroupList(update.getMessage().getChatId().toString());
                return;
            }
            String groupId = update.getMessage().getText().split(SPACE)[1];
            String chatId = update.getMessage().getChatId().toString();
            if(isNumeric(groupId)){
                GroupDiscussionInfo groupDiscussionInfo = javaRushGroupClient.getGroupById(Integer.parseInt(groupId));
                if (isNull(groupDiscussionInfo.getId())){
                    sendGroupNotFound(chatId, groupId);
                }
                GroupSub savedGroupSub = groupSubService.save(chatId, groupDiscussionInfo);
                sendBotMessageService.sendMessage(chatId, "Подписал на группу " + savedGroupSub.getTitle());
            }else {
                sendGroupNotFound(chatId, groupId);
            }
        }
    }
    private void sendGroupNotFound(String chatId, String groupId) {
        String groupNotFoundMessage = "Нет группы с ID = \"%s\"";
        sendBotMessageService.sendMessage(chatId, String.format(groupNotFoundMessage, groupId));
    }
    private void sendGroupList(String chatId) {
        String groupList = javaRushGroupClient.getGroupList(GroupRequestArgs.builder().build())
                .stream().
                map(groupInfo -> String.format("%s - %s \n", groupInfo.getTitle(), groupInfo.getId()))
                .collect(Collectors.joining());
        String message = "Чтобы подписаться на группу - передай комадну вместе с ID группы. \n" +
                "Например: /addGroupSub 16. \n\n" +
                "я подготовил список всех групп - выберай какую хочешь :) \n\n" +
                "имя группы - ID группы \n\n" +
                "%s";
        sendBotMessageService.sendMessage(chatId, String.format(message, groupList));
    }
}
