package com.github.samen66.javarushtelegrambot.command;

import com.github.samen66.javarushtelegrambot.entity.GroupSub;
import com.github.samen66.javarushtelegrambot.entity.TelegramUser;
import com.github.samen66.javarushtelegrambot.service.GroupSubService;
import com.github.samen66.javarushtelegrambot.service.SendBotMessageService;
import com.github.samen66.javarushtelegrambot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.ws.rs.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.samen66.javarushtelegrambot.command.CommandName.DELETE_GROUP_SUB;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public class DeleteGroupSubCommand implements Command{

    private final SendBotMessageService sendBotMessageService;
    private final GroupSubService groupSubService;
    private final TelegramUserService telegramUserService;

    public DeleteGroupSubCommand(SendBotMessageService sendBotMessageService, GroupSubService groupSubService, TelegramUserService telegramUserService) {
        this.sendBotMessageService = sendBotMessageService;
        this.groupSubService = groupSubService;
        this.telegramUserService = telegramUserService;
    }

    @Override
    public void execute(Update update) {
        if (update.hasMessage()){
            if (update.getMessage().hasText() &&
                    update.getMessage().getText().equals(DELETE_GROUP_SUB.getCommandName())){
                sendListGroupSub(update.getMessage().getChatId().toString());
                return;
            }
            String groupId = update.getMessage().getText().split(SPACE)[1];
            String chatId = update.getMessage().getChatId().toString();
            if (isNumeric(groupId)){
                Optional<GroupSub> optionalGroupSub = groupSubService.findById(Integer.parseInt(groupId));
                if (optionalGroupSub.isPresent()){
                    GroupSub groupSub = optionalGroupSub.get();
//                    groupSub.getUsers().remove(chatId);
                    TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
                    groupSub.getUsers().remove(telegramUser);
                    groupSubService.save(groupSub);
                    sendBotMessageService.sendMessage(chatId, String.format("Удалил подписку на группу: %s", groupSub.getTitle()));
                }else{
                    sendBotMessageService.sendMessage(chatId, "Не нашел такой группы =/");
                }
            } else {
                sendBotMessageService.sendMessage(chatId, "неправильный формат ID группы.\n " +
                        "ID должно быть целым положительным числом");
            }

        }
    }

    private void sendListGroupSub(String chatId) {
        TelegramUser telegramUser = telegramUserService.findByChatId(chatId).orElseThrow(NotFoundException::new);
        //TODO exception
        List<GroupSub> groupSubList = telegramUser.getGroupSubs();
        String message;
        String listGroupSub = "";
        if (CollectionUtils.isEmpty(groupSubList)){
            message = "Пока нет подписок на группы. Чтобы добавить подписку напиши /addGroupSub";
        }else{
            message = "Чтобы удалить подписку на группу - передай комадну вместе с ID группы. \n" +
                    "Например: /deleteGroupSub 16 \n\n" +
                    "я подготовил список всех групп, на которые ты подписан) \n\n" +
                    "имя группы - ID группы \n\n" +
                    "%s";
            listGroupSub = groupSubList.stream().
                    map(groupSub -> String.format("%s - %s\n", groupSub.getTitle(), groupSub.getId()))
                    .collect(Collectors.joining());
        }


        sendBotMessageService.sendMessage(chatId, String.format(message, listGroupSub));
    }
}
